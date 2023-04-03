package com.bsep.admin.pki.service;

import com.bsep.admin.pki.dto.CertificateOptionDto;
import org.bouncycastle.asn1.x509.*;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtensionConverter {
	public CertificateOptionDto extensionToCertificateOptionDto(String name, X509Certificate cert) throws IOException {
		return switch (name) {
			case "Basic Constraints" -> basicConstraintsToCertificateOptionDto(cert);
			case "Key Usage" -> keyUsageToCertificateOptionDto(cert);
			case "Extended Key Usage" -> extendedKeyUsageToCertificateOptionDto(cert);
			default -> null;
		};
	}

	private CertificateOptionDto basicConstraintsToCertificateOptionDto(X509Certificate cert) throws IOException {
		if (cert.getBasicConstraints() == -1) {
			return null;
		}
		BasicConstraints basicConstraints = new BasicConstraints(cert.getBasicConstraints());
		boolean isCA = basicConstraints.isCA();
		int pathLen = basicConstraints.getPathLenConstraint().intValue();

		List<CertificateOptionDto> options = new ArrayList<>();
		options.add(new CertificateOptionDto("Subject is CA", null, Boolean.toString(isCA), "checkbox"));
		options.add(new CertificateOptionDto("Path length", null, Integer.toString(pathLen), "number"));

		return new CertificateOptionDto("Basic Constraints", options, null, "");
	}

	private CertificateOptionDto keyUsageToCertificateOptionDto(X509Certificate cert)  {
		// convert list of boolean to int by powering 2
		if (cert.getKeyUsage() == null) {
			return null;
		}
		boolean[] keyUsageBits = cert.getKeyUsage();
		String[] keyUsageNames = {
				"Digital Signature", "Non Repudiation", "Key Encipherment",
				"Data Encipherment", "Key Agreement", "Key Cert Sign",
				"CRL Sign", "Encipher Only", "Decipher Only"
		};
		List<CertificateOptionDto> options = new ArrayList<>();

		for (int i = 0; i < keyUsageBits.length; i++) {
			if(i >= keyUsageNames.length) break;
			options.add(new CertificateOptionDto(keyUsageNames[i], null, keyUsageBits[i] ? "true" : "false", "checkbox"));
		}
		if (keyUsageBits[keyUsageBits.length - 1]) {
			options.get(options.size() - 1).setValue("true");
		}

		return new CertificateOptionDto("Key Usage", options, null, "");
	}

	private CertificateOptionDto extendedKeyUsageToCertificateOptionDto(X509Certificate cert) throws IOException {
//		ExtendedKeyUsage extendedKeyUsage = ExtendedKeyUsage.getInstance(cert.getExtensionValue(Extension.extendedKeyUsage.getId()));
		List<String> extendedKeyUsage = null;
		try {
			extendedKeyUsage = cert.getExtendedKeyUsage();
		} catch (CertificateParsingException e) {
			return null;
		}
		if (extendedKeyUsage == null) {
			return null;
		}
		String[] keyPurposeNames = {
				"Server Authentication", "Client Authentication", "Code Signing",
				"Email Protection", "Time Stamping", "OCSP Signing", "Any Extended Key Usage"
		};
		HashMap<String, String> keyPurposeOids = new HashMap<>();
		keyPurposeOids.put(KeyPurposeId.id_kp_serverAuth.toString(), "Server Authentication");
		keyPurposeOids.put(KeyPurposeId.id_kp_clientAuth.toString(), "Client Authentication");
		keyPurposeOids.put(KeyPurposeId.id_kp_codeSigning.toString(), "Code Signing");
		keyPurposeOids.put(KeyPurposeId.id_kp_emailProtection.toString(), "Email Protection");
		keyPurposeOids.put(KeyPurposeId.id_kp_timeStamping.toString(), "Time Stamping");
		keyPurposeOids.put(KeyPurposeId.id_kp_OCSPSigning.toString(), "OCSP Signing");
		keyPurposeOids.put(KeyPurposeId.anyExtendedKeyUsage.toString(), "Any Extended Key Usage");

		List<CertificateOptionDto> options = new ArrayList<>();
		for (String oid : extendedKeyUsage) {
			String name = keyPurposeOids.get(oid);
			options.add(new CertificateOptionDto(name, null, "true", "checkbox"));
		}
		for (String name : keyPurposeNames) {
			if (options.stream().noneMatch(o -> o.getName().equals(name))) {
				options.add(new CertificateOptionDto(name, null, "false", "checkbox"));
			}
		}

		return new CertificateOptionDto("Extended Key Usage", options, null, "");
	}

}
