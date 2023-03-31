package com.bsep.admin.pki;

import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.nio.file.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
public class KeyService {
    final String STATIC_PATH = "src/main/resources/static";
    final String STATIC_PATH_TARGET = "target/classes/static";
    private static final String PRIVATE_KEY_FILE_NAME = "private_key.pem";
    private static final String PUBLIC_KEY_FILE_NAME = "public_key.pem";

    public KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return new KeyPair(publicKey, privateKey);
    }

    public void storeKeys(KeyPair keyPair, String user) throws Exception {
        this.storeKeysToFolder(keyPair, user, STATIC_PATH);
        this.storeKeysToFolder(keyPair, user, STATIC_PATH_TARGET);
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

    public String findPublicKeyForUser(String user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String filePath = STATIC_PATH_TARGET + "/" + user + "/" + PUBLIC_KEY_FILE_NAME;;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                FileInputStream fis = new FileInputStream(file);
                byte[] content = fis.readAllBytes();
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(content);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                fis.close();
                return publicKey.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Public key for user " + user + "does not exist");
        }
        return "";
    }
}
