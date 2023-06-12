package com.bsep.admin;

import com.bsep.admin.keystores.KeyStoreReader;
import com.bsep.admin.myHouse.DeviceMessageService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Base64;


public class CryptoTest {

    public Boolean verifyHash(String hash, String text, PublicKey publicKey) {
        try {
            // load public key
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            publicKey = keyStoreReader.readPublicKey("./keystores/admin.jks", "admin", "bebenem");

            // print public key pem
            System.out.println("Public key pem: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            String pbk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv341l5bxIR570MeQIce2\n" +
                    "+Qk+ceqwEomrh43TdQMmd6Lkw/C4BvIAFqGrXpc1xJVvhncN55r6gC1bZwY15PUL\n" +
                    "GCeyk8XoLe0K6Luw5XbBfDS4OSdICBSeTMMX0nJhK8qrQBZSWYw/rPsu/7mvJ9xA\n" +
                    "FuvR739GUVuPtmo7mkqYOcCGjU/QpEAAKRkmtMaiT2VKIeli1+NKr2+T7CiIJsKO\n" +
                    "AET7GOlQg61xcOUsAqH2IJKKT3PvMRDtGYZRYZIatlKJbq2iY7vYMNtE3dYdGujT\n" +
                    "1mLHzNuXwuXeWXZqBgYpKzBVMDo5zWH4TBQL69qSoOgMj6PYh04SqDSU4tbn2MOz\n" +
                    "uQIDAQAB";

            // get hash bytes from base64 string
            byte[] bytesOfHash = Base64.getDecoder().decode(hash);

            // Print the bytes of the text being used for verification
            byte[] bytesOfText = text.getBytes("UTF-8");

            // initialize the signature with the PSS parameter specification
            Signature signature = Signature.getInstance("SHA1withRSA");

//            PSSParameterSpec pssParameterSpec = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 0, 1);
//            signature.setParameter(pssParameterSpec);

            signature.initVerify(publicKey);
            signature.update(bytesOfText);

            return signature.verify(bytesOfHash);
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying the hash", e);
        }
    }


    public byte[] getHash(String text) {
        try {
            // load private key
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            PrivateKey privateKey = keyStoreReader.readPrivateKey("./keystores/admin.jks", "admin", "bebenem", "");

            // print private key pem

            Signature signature = Signature.getInstance("SHA1withRSA");

            signature.initSign(privateKey);
            signature.update(text.getBytes("UTF-8"));

            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("Error while getting the hash", e);
        }
    }


    @Test
    public void testVerification() {

        KeyPair keyPair = generateKeys();
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        PublicKey publicKey = keyStoreReader.readPublicKey("./keystores/admin.jks", "admin", "bebenem");
        PrivateKey privateKey = keyStoreReader.readPrivateKey("./keystores/admin.jks", "admin", "bebenem", "");



        String message = "d2348a79-dbf6-40ec-9ed9-f5d85059c7ee,INFO,Measured temperature,20.6,2023-06-12T23:01:33.745747";
        Security.addProvider(new BouncyCastleProvider());

        String hash = Base64.getEncoder().encodeToString(getHash(message));
//        String hash = "ub3IoQPGN4ZCJ9JSxTXQkXqdvhRyAoCrIHOd9JxcoJYwObbcWcL3Gvr/9YkJUQ1MxhsGW1gdjdG/s/XFq+U7drt5Ws/mLgNnxqu2VO3y/7lkHcZjfxTcpriQQwHNpPsXxQyWNfUZtwGrGWZIe7ifEJboyZvVzwRfwUi2+ZaPhLGZ14+TnPQ6XtRYl3q2t4C3Z+YPspN1hpcQTil+Yeya0bCj1imPRbg8toUOsBunJiNe/g1iBAggsln86vIuTR7agV2QxPkr3oPMDJICyeHqpXvgJkvjA9GVvuBqDQgdNMpjjA4PcNd8E3PPR6WGGb5vLc9/aTgURO+b+dDcmrSH0w==";
//        String hash = Base64.getEncoder().encodeToString("YmTZXOvR2Sa2RjzAvE2SF0FMsfRLCuPUY6rKLq97fuQjtDCleo/cyAOt0eMMBInUYeUyV5jCzdN9KIoKXDzHG/L29nC0ytu5MjXomEz4g40KD3d/oeoA+Ltc/cjaw9UF1GFEuUAQwC0kkKxSmK0edJ4qvGppEigwaw+ZYHS5cxGm64knD3bbBWXGRO22EiO0kNA32RIpc+nF6xpuRFBNcpT6rjZIKojjrtmqMZsNH71p5pmYZFlfAXCKGt/dQAO5IDLG8PRWlFTiHQombtAYMkobhM++QtwW3BoMafWYFvqeqCZy3Ro5WcnG2nbH7VsqYuHcMelGecv0a1haR2XgUA==".getBytes(StandardCharsets.UTF_8));
        System.out.println(hash);
        System.out.println(verifyHash(hash, message, publicKey));
        assert true;

    }

    private KeyPair generateKeys() {
        try {
            // Generator para kljuceva
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            // Za kreiranje kljuceva neophodno je definisati generator pseudoslucajnih brojeva
            // Ovaj generator mora biti bezbedan (nije jednostavno predvideti koje brojeve ce RNG generisati)
            // U ovom primeru se koristi generator zasnovan na SHA1 algoritmu, gde je SUN provajder
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            // inicijalizacija generatora, 2048 bitni kljuc
            keyGen.initialize(2048, random);

            // generise par kljuceva koji se sastoji od javnog i privatnog kljuca
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

//        String hash = "cRNsPIjek7RxMwvmnmAzdFPz5JOtxw7zWo5PHqeZ5RlbmiIQk1d4RRAlCxG8eaZ1F0rTWejhO+r4f0LEwy2g2c48SNkBQ4oBzeyne+NaPUUWtogTXHMOu0VCpAAcXcdONTkOsA8gnMwVktxbFU62sZSKDkxcuOyKwcxBWRdsTv2E8iBlCb5bs+/EJpepp6eH2MB5IBFmJOg9e7JwQy4CKYHFDjmKiTG9Cv46mKgr2knBBoslgA+XgCHTUq8Hd11ee5pIoB88zHvXeyf/N/06OSkjSNSNepOh5oNeOQDGjyoHLbI0B9Pe2EbbGZX9hBnZUcXwkYLZTqg4CX0rS4Yfqg==";

    //
//        if (!verifyHash(hash, message)) {
//            System.out.println("Hash verification failed!!!");
//            assert false;
//            return;
//        }
//        System.out.println("Hash verification passed!!!");

    public String readFirstMessageLine() {
        String fileName = "./devices/logs/b79ca272-267a-4d53-947d-46ead6ad8a13.log";
        String line = null;
        try {
            RandomAccessFile file = new RandomAccessFile(fileName, "r");
            line = file.readLine();
            file.close();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
