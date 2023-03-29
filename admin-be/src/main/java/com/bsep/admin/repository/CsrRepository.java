package com.bsep.admin.repository;

import com.bsep.admin.model.Csr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CsrRepository extends JpaRepository<Csr, UUID> {
    Optional<Csr> findByEmail(String email);
}
