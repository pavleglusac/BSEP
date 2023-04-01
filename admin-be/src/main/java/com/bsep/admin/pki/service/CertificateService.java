package com.bsep.admin.pki.service;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.data.SubjectData;
import com.bsep.admin.exception.CertificateAlreadyRevokedException;
import com.bsep.admin.keystores.KeyStoreReader;
import com.bsep.admin.keystores.KeyStoreWriter;
import com.bsep.admin.model.CertificateRevocation;
import com.bsep.admin.model.Csr;
import com.bsep.admin.model.CsrStatus;
import com.bsep.admin.pki.dto.CertificateDto;
import com.bsep.admin.pki.dto.CertificateOptionDto;
import com.bsep.admin.pki.dto.CertificateRevocationDto;
import com.bsep.admin.pki.dto.CsrDto;
import com.bsep.admin.repository.CertificateRevocationRepository;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService {


	@Autowired
	private AdminService adminService;

	@Autowired
	private CsrService csrService;

	@Autowired
	private KeyService keyService;

	@Autowired
	private CertificateRevocationRepository certificateRevocationRepository;

	BigInteger serialNumber = BigInteger.valueOf(3);

	public void processCertificate(CertificateDto cert) throws OperatorCreationException, CertificateException, NoSuchAlgorithmException, KeyStoreException, InvalidKeySpecException {
		Csr csr = csrService.getCsrByUser(cert.getCsrId());
		int len = adminService.getAdminChain().length;
		IssuerData issuerData = adminService.getAdminIssuerData(len - cert.getHierarchyLevel());

		SubjectData subjectData = csrToSubjectData(csr, cert);
		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
		builder = builder.setProvider("BC");

		ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
		X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
				issuerData.getX500name(),
				new BigInteger(subjectData.getSerialNumber()),
				subjectData.getStartDate(),
				subjectData.getEndDate(),
				subjectData.getX500name(),
				subjectData.getPublicKey());

		processExtensions(certGen, cert);

		X509CertificateHolder certHolder = certGen.build(contentSigner);
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		X509Certificate certificate = certConverter.getCertificate(certHolder);

		ArrayList<X509Certificate> chainList = getChainList(cert.getHierarchyLevel(), certificate);

		// save to keystore
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		keyStoreWriter.loadKeyStore("keystores/admin.jks", "admin".toCharArray());
		keyStoreWriter.writeChain(csr.getEmail(),  issuerData.getPrivateKey(), "admin".toCharArray(), chainList);
		keyStoreWriter.saveKeyStore("keystores/admin.jks", "admin".toCharArray());

		// save to db
		csr.setStatus(CsrStatus.APPROVED);

		certificateRevocationRepository.deleteByUserEmail(csr.getEmail());
		csrService.saveCsr(csr);
	}

	private SubjectData csrToSubjectData(Csr csr, CertificateDto cert) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SubjectData subjectData = new SubjectData();
		SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
		// convert localdatetime to date
		Date startDate = Date.from(cert.getValidityStart().atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(cert.getValidityEnd().atZone(ZoneId.systemDefault()).toInstant());
		String sn = serialNumber.toString();
		serialNumber = serialNumber.add(BigInteger.ONE);
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		if(csr.getCommonName() != null) {
			builder.addRDN(BCStyle.CN, csr.getCommonName());
		}
		if(csr.getCountry() != null) {
			builder.addRDN(BCStyle.C, csr.getCountry());
		}
		if(csr.getOrganization() != null) {
			builder.addRDN(BCStyle.O, csr.getOrganization());
		}
		if(csr.getOrganizationalUnit() != null) {
			builder.addRDN(BCStyle.OU, csr.getOrganizationalUnit());
		}
		if(csr.getEmail() != null) {
			builder.addRDN(BCStyle.E, csr.getEmail());
		}
		if(csr.getSurname() != null) {
			builder.addRDN(BCStyle.SURNAME, csr.getSurname());
		}
		if(csr.getGivenName() != null) {
			builder.addRDN(BCStyle.GIVENNAME, csr.getGivenName());
		}
		subjectData.setStartDate(startDate);
		subjectData.setEndDate(endDate);
		subjectData.setSerialNumber(sn);
		subjectData.setX500name(builder.build());
		PublicKey publicKey = keyService.findPublicKeyForUser(csr.getEmail());
		subjectData.setPublicKey(publicKey);
		return subjectData;
	}

	private void processExtensions(X509v3CertificateBuilder certGen, CertificateDto cert) {
		List<CertificateOptionDto> extensionsDto = cert.getExtensions();
		ExtensionBuilder extensionBuilder = new ExtensionBuilder(certGen);
		for (CertificateOptionDto extensionDto : extensionsDto) {
			try {
				extensionBuilder.buildExtension(extensionDto.getName(), extensionDto.getOptions());
			} catch (CertIOException e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<X509Certificate> getChainList(int hierarchyLevel, X509Certificate certificate) {
		X509Certificate[] chain = adminService.getAdminChain();
		chain = Arrays.copyOfRange(chain, chain.length - hierarchyLevel, chain.length);
		ArrayList<X509Certificate> chainList = new ArrayList<>(Arrays.asList(chain));
		chainList = chainList.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		chainList.add(0, certificate);
		return chainList;
	}

	public X509Certificate[] getCertificateChain(String alias) throws KeyStoreException {
		KeyStoreReader keyStoreReader = new KeyStoreReader();
		Certificate[] certChain = keyStoreReader.readCertificateChain(adminService.KEYSTORE_FILE, "admin", alias);
		return Arrays.stream(certChain)
					 .map(cert -> (X509Certificate) cert)
					 .toArray(X509Certificate[]::new);
	}

	private SubjectData generateSubjectData() {
		try {
			KeyPair keyPairSubject = generateKeyPair();
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse("2022-03-01");
			Date endDate = iso8601Formater.parse("2024-03-01");
			String sn = "4";
			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
			builder.addRDN(BCStyle.CN, "Marija Kovacevic");
			builder.addRDN(BCStyle.SURNAME, "Kovacevic");
			builder.addRDN(BCStyle.GIVENNAME, "Marija");
			builder.addRDN(BCStyle.O, "UNS-FTN");
			builder.addRDN(BCStyle.OU, "Katedra za informatiku");
			builder.addRDN(BCStyle.C, "RS");
			builder.addRDN(BCStyle.E, "marija.kovacevic@uns.ac.rs");

			builder.addRDN(BCStyle.UID, "654321");
			return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CertificateDto> findAllCertificate() {
		KeyStoreReader keyStoreReader = new KeyStoreReader();
		List<CertificateDto> certificatesDto = new ArrayList<>();
		List<X509Certificate> certificates = keyStoreReader.readAllCertificates(adminService.KEYSTORE_FILE, "admin");
		ExtensionConverter extensionConverter = new ExtensionConverter();
		List<String> extensions = Arrays.asList("Basic Constraints", "Key Usage", "Extended Key Usage");
		for (X509Certificate certificate : certificates) {
			CertificateDto certificateDto = new CertificateDto();
			certificateDto.setExtensions(new ArrayList<>());
			certificateDto.setAlgorithm(certificate.getPublicKey().getAlgorithm());
			certificateDto.setValidityStart(certificate.getNotBefore()
													   .toInstant()
													   .atZone(ZoneId.systemDefault()).toLocalDate()
													   .atStartOfDay());
			certificateDto.setValidityEnd(certificate.getNotAfter()
													 .toInstant()
													 .atZone(ZoneId.systemDefault()).toLocalDate()
													 .atStartOfDay());
			// get email from subject
			String email = getEmailFromCertificate(certificate);
			certificateDto.setCsrId(email);
//			certificateDto.setCsrDto(csrService(email));
			CsrDto csrDto = readCsrFromRdns(certificate);
			certificateDto.setCsrId(email);
			certificateDto.setCsrDto(csrDto);
			for (String extensionName : extensions) {
				try {
					CertificateOptionDto extension = extensionConverter.extensionToCertificateOptionDto(extensionName, certificate);
					if(extension != null) {
						certificateDto.getExtensions().add(extension);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// get issuer common name
			String issuerName = certificate.getIssuerX500Principal().getName();
			String issuerCommonName = readCommonName(issuerName);
			if (issuerCommonName != null) {
				if (issuerCommonName.startsWith("Root")) {
					certificateDto.setHierarchyLevel(1);
				} else if (issuerCommonName.startsWith("First")) {
					certificateDto.setHierarchyLevel(2);
				} else {
					certificateDto.setHierarchyLevel(3);
				}
			}
			Optional<CertificateRevocation> revocation = certificateRevocationRepository.findByUserEmail(email);
			certificateDto.setIsRevoked(revocation.isPresent());
			certificatesDto.add(certificateDto);
		}
		return certificatesDto;
	}

	private String readCommonName(String name) {
		String[] parts = name.split(",");
		for (String part : parts) {
			if (part.contains("CN=")) {
				return part.substring(3);
			}
		}
		return null;
	}

	private CsrDto readCsrFromRdns(X509Certificate cert) {
		X500Name x500name = new X500Name(cert.getSubjectX500Principal().getName());
		RDN[] rdns = x500name.getRDNs();
		CsrDto csrDto = new CsrDto();
		for (RDN rdn : rdns) {
			AttributeTypeAndValue[] atv = rdn.getTypesAndValues();
			for (AttributeTypeAndValue attributeTypeAndValue : atv) {
				if (attributeTypeAndValue.getType().equals(BCStyle.CN)) {
					csrDto.setCommonName(attributeTypeAndValue.getValue().toString());
				} else if (attributeTypeAndValue.getType().equals(BCStyle.SURNAME)) {
					csrDto.setSurname(attributeTypeAndValue.getValue().toString());
				} else if (attributeTypeAndValue.getType().equals(BCStyle.GIVENNAME)) {
					csrDto.setGivenName(attributeTypeAndValue.getValue().toString());
				} else if (attributeTypeAndValue.getType().equals(BCStyle.O)) {
					csrDto.setOrganization(attributeTypeAndValue.getValue().toString());
				} else if (attributeTypeAndValue.getType().equals(BCStyle.OU)) {
					csrDto.setOrganizationalUnit(attributeTypeAndValue.getValue().toString());
				} else if (attributeTypeAndValue.getType().equals(BCStyle.C)) {
					csrDto.setCountry(attributeTypeAndValue.getValue().toString());
				} else if (attributeTypeAndValue.getType().equals(BCStyle.E)) {
					csrDto.setEmail(attributeTypeAndValue.getValue().toString());
				}
			}
		}
		return csrDto;
	}

	public String getEmailFromCertificate(X509Certificate cert) {
		X500Name x500Name = new X500Name(cert.getSubjectX500Principal().getName());
		RDN[] rdns = x500Name.getRDNs(BCStyle.EmailAddress);
		if (rdns.length == 0) {
			return null;
		}
		RDN emailAddressRDN = rdns[0];
		if (emailAddressRDN != null) {
			return emailAddressRDN.getFirst().getValue().toString();
		} else {
			return null;
		}
	}

	public String distributeCertificate(String email) {
		try{
			KeyStoreReader keyStoreReader = new KeyStoreReader();
			String publicKey = this.keyService.findPublicKeyForUser(email).toString();
			String privateKey = this.keyService.findPrivateKeyForUser(email);
			//find certificate for email
			Certificate[] certificateChain = keyStoreReader.readCertificateChain(adminService.KEYSTORE_FILE, "admin", email);
			X509Certificate certificate = (X509Certificate) certificateChain[0];
			//send all info via email
			System.out.println(certificate);

			return "Certificate, public and private key for user " + email + " are sent via email.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Cannot distribute certificate for user " + email;
		}
	}

	public Boolean validateCertificate(String email) {
		try {
			KeyStoreReader keyStoreReader = new KeyStoreReader();
			Certificate[] certificateChain = keyStoreReader.readCertificateChain(adminService.KEYSTORE_FILE, "admin", email);
			// validate entire chain
			int i = 0;
			for (Certificate certificate : certificateChain) {
				X509Certificate x509Certificate = (X509Certificate) certificate;
				x509Certificate.checkValidity();
				if (i < certificateChain.length - 1) {
					x509Certificate.verify(certificateChain[i + 1].getPublicKey());
				}
				i++;
				Optional<CertificateRevocation> revocation = certificateRevocationRepository
						.findByUserEmail(getEmailFromCertificate(x509Certificate));
				if (revocation.isPresent()) return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public void revokeCertificate(CertificateRevocationDto dto) {
		CertificateRevocation certificateRevocation = new CertificateRevocation();
		certificateRevocation.setUserEmail(dto.getEmail());
		certificateRevocation.setTimestamp(LocalDateTime.now());
		try {
			certificateRevocationRepository.save(certificateRevocation);
		} catch (DataIntegrityViolationException e) {
			throw new CertificateAlreadyRevokedException();
		}
	}

}
