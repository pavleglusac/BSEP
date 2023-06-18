package com.bsep.admin.repository;

import com.bsep.admin.model.CertificateRevocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

public interface CertificateRevocationRepository extends JpaRepository<CertificateRevocation, UUID> {
    Optional<CertificateRevocation> findByUserEmail(String userEmail);
    Optional<CertificateRevocation> findByUserEmailAndSerialNumber(String userEmail, BigInteger serialNumber);
    long deleteByUserEmail(String userEmail);
}
