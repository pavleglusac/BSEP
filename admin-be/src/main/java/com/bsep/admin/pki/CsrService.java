package com.bsep.admin.pki;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.data.SubjectData;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cert.path.CertPath;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
public class CsrService {

	@Autowired
	private AdminService adminService;

	private static final String KEYSTORE_TYPE = "JKS";
	private static final String KEYSTORE_FILE = "keystore.jks";
	private static final String KEYSTORE_PASSWORD = "changeit";
	private static final String ALIAS_PREFIX = "certificate-";

	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	private static final int VALIDITY_PERIOD = 365 * 24 * 60 * 60;


	public CsrService() {

	}

	private PrivateKey readPrivateKey() {
		try {
			KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
			keyStore.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());
			return (PrivateKey) keyStore.getKey("admin", KEYSTORE_PASSWORD.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private X509Certificate readCertificate() {
		return null;
	}

	public void processCsr(String csr) throws Exception {
		// Convert the CSR string to a PKCS10CertificationRequest object
		JcaPKCS10CertificationRequest jcaCsr = new JcaPKCS10CertificationRequest(csr.getBytes());

		String subjectName = jcaCsr.getSubject().toString();
		PublicKey publicKey = jcaCsr.getPublicKey();



		var subject = new SubjectData(publicKey, jcaCsr.getSubject(), "1", new Date(), new Date());
		var issuer = adminService.getAdminIssuerData();

		X509Certificate certificate = issueCertificate(subject, issuer);

		storeCertificate(certificate, subjectName);
	}

	public X509Certificate issueCertificate(SubjectData subjectData, IssuerData issuerData) throws Exception {
		try {
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM);

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

			return certConverter.getCertificate(certHolder);
		} catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isValidSubject(String subjectName) {
		// Validate the subject name against the Admin Application's database
		// ...
		return true;
	}

	private boolean isValidPublicKey(PublicKey publicKey) {
		// Validate the public key against the Admin Application's database
		// ...
		return true;
	}

	public void storeCertificate(X509Certificate certificate, String name) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
		FileInputStream fis = new FileInputStream(KEYSTORE_FILE);
		keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());

		String alias = ALIAS_PREFIX + name;

		keyStore.setCertificateEntry(alias, certificate);

		FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE);
		keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());

		fis.close();
		fos.close();
	}

}