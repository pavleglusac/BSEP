package com.bsep.admin.pki.service;

import com.bsep.admin.pki.dto.CertificateOptionDto;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtensionBuilder {
	X509v3CertificateBuilder certGen;
	HashMap<String, String> extensionOidMap = new HashMap<>();

	ExtensionBuilder(X509v3CertificateBuilder certGen) {
		this.certGen = certGen;
		extensionOidMap.put("Basic Constraints", "2.5.29.19");
		extensionOidMap.put("Key Usage", "2.5.29.15");
		extensionOidMap.put("Extended Key Usage", "2.5.29.37");
	}
	public void buildExtension(String name, List<CertificateOptionDto> options) throws CertIOException {
		switch (name) {
			case "Basic Constraints" -> buildBasicConstraints(options);
			case "Key Usage" -> buildKeyUsage(options);
			case "Extended Key Usage" -> buildExtendedKeyUsage(options);
		}
	}

	private void buildBasicConstraints(List<CertificateOptionDto> options) throws CertIOException {
		boolean isCA = false;
		int pathLen = 0;
		for (CertificateOptionDto option : options) {
			if (option.getName().equals("Subject is CA")) {
				isCA = Boolean.parseBoolean(option.getValue());
			} else if (option.getName().equals("Path length")) {
				pathLen = Integer.parseInt(option.getValue());
			}
		}
		BasicConstraints basicConstraints;
		if (isCA) {
			basicConstraints = new BasicConstraints(pathLen);
		} else {
			basicConstraints = new BasicConstraints(false);
		}
		certGen.addExtension(new ASN1ObjectIdentifier(extensionOidMap.get("Basic Constraints")), false, basicConstraints);
	}

	private void buildKeyUsage(List<CertificateOptionDto> options) {
		boolean[] keyUsage = new boolean[9];
		for (CertificateOptionDto option : options) {
			switch (option.getName()) {
				case "Encipher only" -> keyUsage[0] = Boolean.parseBoolean(option.getValue());
				case "CRL sign" -> keyUsage[1] = Boolean.parseBoolean(option.getValue());
				case "Certificate signing" -> keyUsage[2] = Boolean.parseBoolean(option.getValue());
				case "Key agreement" -> keyUsage[3] = Boolean.parseBoolean(option.getValue());
				case "Data encipherment" -> keyUsage[4] = Boolean.parseBoolean(option.getValue());
				case "Key encipherment" -> keyUsage[5] = Boolean.parseBoolean(option.getValue());
				case "Non repudiation" -> keyUsage[6] = Boolean.parseBoolean(option.getValue());
				case "Digital signature" -> keyUsage[7] = Boolean.parseBoolean(option.getValue());
				case "Decipher only" -> keyUsage[8] = Boolean.parseBoolean(option.getValue());
			}
		}
		int keyUsageInt = 0;
		for (int i = 0; i < keyUsage.length; i++) {
			if (keyUsage[i]) {
				keyUsageInt += Math.pow(2, i);
			}
		}
		KeyUsage keyUsageObj = new KeyUsage(keyUsageInt);
		try {
			certGen.addExtension(new ASN1ObjectIdentifier(extensionOidMap.get("Key Usage")), false, keyUsageObj);
		} catch (CertIOException e) {
			e.printStackTrace();
		}
	}

	private void buildExtendedKeyUsage(List<CertificateOptionDto> options) {
		List<KeyPurposeId> keyPurposeIds = new ArrayList<>();
		for (CertificateOptionDto option : options) {
			KeyPurposeId keyPurposeId = switch (option.getName()) {
				case "Server authentication" -> KeyPurposeId.id_kp_serverAuth;
				case "Client authentication" -> KeyPurposeId.id_kp_clientAuth;
				case "Code signing" -> KeyPurposeId.id_kp_codeSigning;
				case "Email protection" -> KeyPurposeId.id_kp_emailProtection;
				case "Time stamping" -> KeyPurposeId.id_kp_timeStamping;
				case "OCSP signing" -> KeyPurposeId.id_kp_OCSPSigning;
				case "Any extended key usage" -> KeyPurposeId.anyExtendedKeyUsage;
				default -> null;
			};
			if (keyPurposeId != null && Boolean.parseBoolean(option.getValue())) {
				keyPurposeIds.add(keyPurposeId);
			}
		}

		ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage(keyPurposeIds.toArray(new KeyPurposeId[0]));
		try {
			certGen.addExtension(new ASN1ObjectIdentifier(extensionOidMap.get("Extended Key Usage")), false, extendedKeyUsage);
		} catch (CertIOException e) {
			e.printStackTrace();
		}
	}
}
