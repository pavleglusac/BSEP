package com.bsep.admin.pki.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.*;

@Service
public class KeyService {
    final String STATIC_PATH = "src/main/resources/static";
    final String STATIC_PATH_TARGET = "target/classes/static";
    private static final String PRIVATE_KEY_FILE_NAME = "private_key.pem";
    private static final String PUBLIC_KEY_FILE_NAME = "public_key.pem";
    private static final String CERTIFICATE_FILE_NAME = "certificate.cer";

    public KeyPair generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyPairGenerator.initialize(2048, random);
        return keyPairGenerator.generateKeyPair();
    }

    public void storeKeys(KeyPair keyPair, String user) throws Exception {
        this.storeKeysToFolder(keyPair, user, STATIC_PATH);
        this.storeKeysToFolder(keyPair, user, STATIC_PATH_TARGET);
    }

    public void storeCertificate(String user, X509Certificate certificate) throws Exception {
        this.storeCertificateToFolder(certificate, user, STATIC_PATH);
        this.storeCertificateToFolder(certificate, user, STATIC_PATH_TARGET);
    }

    public void storeCertificateToFolder(X509Certificate certificate, String user, String path) throws CertificateEncodingException {
        String folderPath = path + "/" + user;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String filePathPrivate = folderPath + "/" + CERTIFICATE_FILE_NAME;
        try {
            FileOutputStream fos = new FileOutputStream(filePathPrivate);
            fos.write(certificate.getEncoded());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void storeKeysToFolder(KeyPair keyPair, String user, String path) {
        String folderPath = path + "/" + user;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        String filePathPrivate = folderPath + "/" + PRIVATE_KEY_FILE_NAME;
        String filePathPublic = folderPath + "/" + PUBLIC_KEY_FILE_NAME;
        try {
            FileOutputStream writer = new FileOutputStream(filePathPrivate);
            writer.write(keyPair.getPrivate().getEncoded());
            writer = new FileOutputStream(filePathPublic);
            writer.write(keyPair.getPublic().getEncoded());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PublicKey findPublicKeyForUser(String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return (PublicKey) this.findKeyForUser(user, PUBLIC_KEY_FILE_NAME);
    }

    public File findPublicKeyFileForUser(String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String filePath = STATIC_PATH_TARGET + "/" + user + "/" + PUBLIC_KEY_FILE_NAME;
        File file = new File(filePath);
        if (file.exists())
            return file;
        throw new RuntimeException("Key for user " + user + " does not exist");
    }

    public File findPrivateKeyFileForUser(String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String filePath = STATIC_PATH_TARGET + "/" + user + "/" + PRIVATE_KEY_FILE_NAME;
        File file = new File(filePath);
        if (file.exists())
            return file;
        throw new RuntimeException("Key for user " + user + " does not exist");
    }

    public File findCertificateForUser(String user) {
        String filePath = STATIC_PATH_TARGET + "/" + user + "/" + CERTIFICATE_FILE_NAME;
        File file = new File(filePath);
        if (file.exists())
            return file;
        throw new RuntimeException("Certificate for user " + user + " does not exist");
    }

    public String findPrivateKeyForUser(String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return this.findKeyForUser(user, PRIVATE_KEY_FILE_NAME).toString();
    }

    private Key findKeyForUser(String user, String keyPath) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String filePath = STATIC_PATH_TARGET + "/" + user + "/" + keyPath;;
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                byte[] content = fis.readAllBytes();
                if (keyPath.equals(PUBLIC_KEY_FILE_NAME)) {
                    X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(content);
                    return keyFactory.generatePublic(keySpecPublic);
                } else{
                    PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(content);
                    return keyFactory.generatePrivate(keySpecPrivate);}
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Cannot read key for user " + user + ".");
            }
        } else {
            throw new RuntimeException("Key for user " + user + " does not exist");
        }
    }
    public KeyPair findKeyPairForUser(String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = (PublicKey) this.findKeyForUser(user, PUBLIC_KEY_FILE_NAME);
        PrivateKey privateKey = (PrivateKey) this.findKeyForUser(user, PRIVATE_KEY_FILE_NAME);
        return new KeyPair(publicKey, privateKey);
    }
}
