package com.bsep.admin.keystores;


import com.bsep.admin.data.IssuerData;
import com.bsep.admin.pki.dto.CertificateDto;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class KeyStoreReader {
	// KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
	// Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
	// - Sertifikati koji ukljucuju javni kljuc
	// - Privatni kljucevi
	// - Tajni kljucevi, koji se koriste u simetricnima siframa
	private KeyStore keyStore;

	public KeyStoreReader() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException | NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
	 * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
	 *
	 * @param keyStoreFile - datoteka odakle se citaju podaci
	 * @param alias        - alias putem kog se identifikuje sertifikat izdavaoca
	 * @param password     - lozinka koja je neophodna da se otvori key store
	 * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
	 */
	public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
		try {
			// Datoteka se ucitava
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, password);

			// Iscitava se sertifikat koji ima dati alias
			Certificate cert = keyStore.getCertificate(alias);

			// Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			return new IssuerData( issuerName, privKey);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
				 | UnrecoverableKeyException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ucitava sertifikat is KS fajla
	 */
	public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());

			if (ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;
			}
		} catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
				 | CertificateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Certificate[] readCertificateChain(String keyStoreFile, String keyStorePass, String alias) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			if (ks.isKeyEntry(alias)) {
				Certificate[] certChain = ks.getCertificateChain(alias);
				return certChain;
			}
		} catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
				 | CertificateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BigInteger findBigestSerialNumber(String keyStoreFile, String keyStorePass) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BigInteger max = new BigInteger("-1");
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			Enumeration<String> aliases = ks.aliases();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				if (ks.isKeyEntry(alias)) {
					Certificate[] certChain = ks.getCertificateChain(alias);
					for (Certificate cert : certChain) {
						X509Certificate x509Certificate = (X509Certificate) cert;
						if (x509Certificate.getSerialNumber().compareTo(max) > 0) {
							max = x509Certificate.getSerialNumber();
						}
					}
				}
			}
			return max;

		} catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
				 | CertificateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public Certificate[] readCertificateChainBySerialNumber(String keyStoreFile, String keyStorePass, String serialNumber) {
		try {
			ArrayList<Certificate> certChain = new ArrayList<Certificate>();
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());
			// iterate over aliases
			Enumeration<String> aliases = ks.aliases();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				boolean found = false;
				if (ks.isKeyEntry(alias)) {
					Certificate[] chain = ks.getCertificateChain(alias);
					for (int i = 0; i < chain.length; i++) {
						X509Certificate x509Cert = (X509Certificate) chain[i];
						if (x509Cert.getSerialNumber().toString().equals(serialNumber)) {
							for (int j = i; j < chain.length; j++) {
								X509Certificate parentCert = (X509Certificate) chain[j];
								certChain.add(parentCert);
							}
							found = true;
							break;
						}
					}
					if(found) {
						break;
					}
				}
			}
			return certChain.toArray(new Certificate[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ucitava privatni kljuc is KS fajla
	 */
	public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());

			if (ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
				 | IOException | UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}


	public List<X509Certificate> readAllCertificates(String keyStoreFile, String password) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, password.toCharArray());
			List<X509Certificate> certificates = new ArrayList<>();
			Enumeration<String> aliases = keyStore.aliases();
			Set<BigInteger> set = new HashSet<>();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				if (keyStore.isKeyEntry(alias)) {
					Certificate[] cert = keyStore.getCertificateChain(alias);
					for (Certificate c : cert) {
						X509Certificate x509Certificate = (X509Certificate) c;
						BigInteger serialNumber = x509Certificate.getSerialNumber();
						if (!set.contains(serialNumber)) {
							set.add(serialNumber);
							certificates.add(x509Certificate);
						}
					}
				}
			}
			return certificates;
		} catch (KeyStoreException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateException | IOException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}
