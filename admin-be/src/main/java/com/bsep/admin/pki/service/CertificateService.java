package com.bsep.admin.pki.service;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.data.SubjectData;
import com.bsep.admin.keystores.KeyStoreReader;
import com.bsep.admin.keystores.KeyStoreWriter;
import com.bsep.admin.model.Csr;
import com.bsep.admin.model.CsrStatus;
import com.bsep.admin.pki.dto.CertificateDto;
import com.bsep.admin.pki.dto.CertificateOptionDto;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService {


	@Autowired
	private AdminService adminService;

	@Autowired
	private CsrService csrService;



	public void processCertificate(CertificateDto cert) throws OperatorCreationException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		Csr csr = csrService.getCsrByUser(cert.getCsrId());

		IssuerData issuerData = adminService.getAdminIssuerData(0);
		SubjectData subjectData = generateSubjectData();
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
		keyStoreWriter.writeChain(csr.getEmail(),  issuerData.getPrivateKey(),  "admin".toCharArray(), chainList);
		keyStoreWriter.saveKeyStore("keystores/admin.jks", "admin".toCharArray());

		// save to db
		csr.setStatus(CsrStatus.APPROVED);
		csrService.saveCsr(csr);
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
		chain = Arrays.copyOfRange(chain, 0, hierarchyLevel);
		ArrayList<X509Certificate> chainList = new ArrayList<>(Arrays.asList(chain));
		chainList = chainList.stream().filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
		chainList.add(certificate);
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

}
