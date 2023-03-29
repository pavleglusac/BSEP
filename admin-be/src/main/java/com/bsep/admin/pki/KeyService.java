package com.bsep.admin.pki;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.security.*;

@Service
public class KeyService {
    private static final String PRIVATE_KEY_PATH = "keystores/private_key.pem";
    private static final String PUBLIC_KEY_PATH = "keystores/public_key.pem";

    public KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return new KeyPair(publicKey, privateKey);
    }

    public void storeKeys(KeyPair keyPair, String user) throws Exception {
        // Write the private key to a file
        try (FileOutputStream out = new FileOutputStream(PRIVATE_KEY_PATH)) {
            out.write(keyPair.getPrivate().getEncoded());
        }

        // Write the public key to a file
        try (FileOutputStream out = new FileOutputStream(PUBLIC_KEY_PATH)) {
            out.write(keyPair.getPublic().getEncoded());
        }
    }
}
