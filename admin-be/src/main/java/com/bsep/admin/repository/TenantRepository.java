package com.bsep.admin.repository;

import com.bsep.admin.model.RealEstate;
import com.bsep.admin.model.Tenant;
import com.bsep.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<User, UUID> {

    List<Tenant> findByRealEstate(RealEstate realEstate);
}
