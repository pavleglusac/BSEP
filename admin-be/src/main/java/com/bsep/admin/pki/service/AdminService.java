package com.bsep.admin.pki.service;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.keystores.KeyStoreReader;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
public class AdminService {

	private IssuerData adminIssuerData;
	private KeyStoreReader keyStoreReader;

	private final String KEYSTORE_FILE = "keystores/admin.jks";

	public AdminService() {
		keyStoreReader = new KeyStoreReader();
		adminIssuerData = keyStoreReader.readIssuerFromStore(KEYSTORE_FILE, "admin", "admin".toCharArray(), "admin".toCharArray());
	}

	public IssuerData getAdminIssuerData() {
		return adminIssuerData;
	}
}
