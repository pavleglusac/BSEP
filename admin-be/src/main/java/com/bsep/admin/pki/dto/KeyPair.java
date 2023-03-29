package com.bsep.admin.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyPair {
    PublicKey publicKey;
    PrivateKey privateKey;
}
