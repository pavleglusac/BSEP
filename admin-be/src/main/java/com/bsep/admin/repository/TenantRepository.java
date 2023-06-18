package com.bsep.admin.repository;

import com.bsep.admin.model.RealEstate;
import com.bsep.admin.model.Tenant;
import com.bsep.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<User, UUID> {

    List<Tenant> findByRealEstate(RealEstate realEstate);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END FROM Tenant u JOIN u.realEstate re JOIN re.devices d WHERE u.id = :userId AND d.id = :deviceId")
    boolean doesUserHaveDevice(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);

    //get real estates by tenant
    @Query("SELECT re FROM Tenant u JOIN u.realEstate re WHERE u.id = :userId")
    List<RealEstate> getRealEstatesByTenant(@Param("userId") UUID userId);
}
