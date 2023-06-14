package com.bsep.admin.repository;

import com.bsep.admin.model.Device;
import com.bsep.admin.model.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RealEstateRepository extends JpaRepository<RealEstate, UUID> {
    @Query("SELECT DISTINCT r FROM RealEstate r JOIN r.devices d WHERE d IN :devices")
    List<RealEstate> findByDevicesInList(@Param("devices") List<Device> devices);

}
