package com.bsep.admin.repository;

import com.bsep.admin.model.CertificateRevocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CertificateRevocationRepository extends JpaRepository<CertificateRevocation, UUID> {
    Optional<CertificateRevocation> findByUserEmail(String userEmail);
}
