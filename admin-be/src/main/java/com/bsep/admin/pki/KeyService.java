package com.bsep.admin.pki;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.nio.file.*;

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
}
