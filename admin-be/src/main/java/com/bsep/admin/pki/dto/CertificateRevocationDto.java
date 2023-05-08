package com.bsep.admin.pki.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigInteger;

@Data
public class CertificateRevocationDto {
    @Email
    private String email;
    private BigInteger serialNumber;
}
