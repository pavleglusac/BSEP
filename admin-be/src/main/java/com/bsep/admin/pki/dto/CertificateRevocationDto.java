package com.bsep.admin.pki.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CertificateRevocationDto {
    private String email;
    private BigInteger serialNumber;
}
