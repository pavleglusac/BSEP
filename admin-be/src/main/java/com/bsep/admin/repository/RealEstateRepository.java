package com.bsep.admin.repository;

import com.bsep.admin.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RealEstateRepository extends JpaRepository<RealEstate, UUID> {
}
