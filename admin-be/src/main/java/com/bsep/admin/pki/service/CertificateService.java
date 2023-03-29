package com.bsep.admin.pki.service;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.data.SubjectData;
import com.bsep.admin.keystores.KeyStoreWriter;
import com.bsep.admin.pki.dto.CertificateDto;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CertificateService {


	@Autowired
	private AdminService adminService;

	public void processCertificate(CertificateDto cert) throws OperatorCreationException, CertificateException {
		// create certificate
		// save to db
		// store to folder (create folder if it doesn't exist)
		IssuerData issuerData = adminService.getAdminIssuerData();
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

		X509CertificateHolder certHolder = certGen.build(contentSigner);
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		X509Certificate certificate =  certConverter.getCertificate(certHolder);

		System.out.println(certificate);
		// save to keystore
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		keyStoreWriter.loadKeyStore("keystores/admin.jks", "admin".toCharArray());
		keyStoreWriter.write("novientry",  issuerData.getPrivateKey(),  "admin".toCharArray(), certificate);
		keyStoreWriter.saveKeyStore("keystores/admin.jks", "admin".toCharArray());
	}

	private SubjectData generateSubjectData() {
		try {
			KeyPair keyPairSubject = generateKeyPair();
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse("2022-03-01");
			Date endDate = iso8601Formater.parse("2024-03-01");
			String sn = "1";
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
