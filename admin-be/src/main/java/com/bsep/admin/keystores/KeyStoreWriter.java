package com.bsep.admin.keystores;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public class KeyStoreWriter {

	private KeyStore keyStore;

	public KeyStoreWriter() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException | NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	public void loadKeyStore(String fileName, char[] password) {
		try {
			if (fileName != null) {
				keyStore.load(new FileInputStream(fileName), password);
			} else {
				keyStore.load(null, password);
			}
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}

	public void saveKeyStore(String fileName, char[] password) {
		try {
			keyStore.store(new FileOutputStream(fileName), password);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, new Certificate[]{certificate});
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public void writeChain(String alias, PrivateKey privateKey, char[] password, Certificate[] certificates) {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, certificates);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}

	public void writeChain(String alias, PrivateKey privateKey, char[] password, List<X509Certificate> certificates) {
		try {
			Certificate[] certs = new Certificate[certificates.size()];
			certs = certificates.toArray(certs);
			keyStore.setKeyEntry(alias, privateKey, password, certs);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
}

