package com.bsep.admin.pki.service;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.data.SubjectData;
import com.bsep.admin.keystores.KeyStoreReader;
import com.bsep.admin.keystores.KeyStoreWriter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
public class AdminService {

	public final String KEYSTORE_FILE = "keystores/admin.jks";
	private IssuerData[] issuerData = new IssuerData[3];
	private X509Certificate[] chain = new X509Certificate[3];
	private KeyStoreReader keyStoreReader;
	private KeyStoreWriter keyStoreWriter;

	public AdminService() throws CertificateException, OperatorCreationException {
		keyStoreReader = new KeyStoreReader();
		keyStoreWriter = new KeyStoreWriter();

		chain[2] = createAdminCertificate("CN=Root", 2, null, null);
		chain[1] = createAdminCertificate("CN=First Intermediate", 1, chain[2], issuerData[2].getPrivateKey());
		chain[0] = createAdminCertificate("CN=Second Intermediate", 0, chain[1], issuerData[1].getPrivateKey());

		keyStoreWriter.loadKeyStore(KEYSTORE_FILE, "admin".toCharArray());
		keyStoreWriter.writeChain("root", issuerData[2].getPrivateKey(), "admin".toCharArray(), chain);
		keyStoreWriter.saveKeyStore(KEYSTORE_FILE, "admin".toCharArray());
	}

	private X509Certificate createAdminCertificate(String commonName, int index, X509Certificate issuerCert, PrivateKey issuerKey) throws CertificateException, OperatorCreationException {
		SubjectData subjectData = new SubjectData();
		subjectData.setX500name(new X500Name(commonName));
		subjectData.setSerialNumber(Integer.toString(index));
		subjectData.setStartDate(new Date());
		subjectData.setEndDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365));
		KeyPair keyPair = generateKeyPair();
		subjectData.setPublicKey(keyPair.getPublic());
		issuerData[index] = new IssuerData();
		issuerData[index].setX500name(subjectData.getX500name());
		issuerData[index].setPrivateKey(keyPair.getPrivate());
		return createSignedCertificate(subjectData, issuerCert, issuerKey == null ? keyPair.getPrivate() : issuerKey);
	}

	private X509Certificate createSignedCertificate(SubjectData subjectData, X509Certificate signer, PrivateKey privateKey) throws OperatorCreationException, CertificateException {
		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
		builder = builder.setProvider("BC");
		ContentSigner contentSigner = builder.build(privateKey);
		X509v3CertificateBuilder certGen;
		if (signer == null) {
			 certGen = new JcaX509v3CertificateBuilder(
				 subjectData.getX500name(),
				new BigInteger(subjectData.getSerialNumber()),
				subjectData.getStartDate(),
				subjectData.getEndDate(),
				subjectData.getX500name(),
				subjectData.getPublicKey());
		} else {
			certGen = new JcaX509v3CertificateBuilder(
					signer,
					new BigInteger(subjectData.getSerialNumber()),
					subjectData.getStartDate(),
					subjectData.getEndDate(),
					subjectData.getX500name(),
					subjectData.getPublicKey());
		}

		X509CertificateHolder certHolder = certGen.build(contentSigner);
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		X509Certificate certificate = certConverter.getCertificate(certHolder);
		return certificate;
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

	public IssuerData getAdminIssuerData(int index) {
		return issuerData[index];
	}

	public X509Certificate[] getAdminChain() {
		return chain;
	}
}
