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

    // find realestate that contains device in list of devices
    @Query("SELECT DISTINCT r FROM RealEstate r JOIN r.devices d WHERE d = :device")
    RealEstate findByDevice(@Param("device") Device device);

    // get all devices by list of real estates
    @Query("SELECT DISTINCT d FROM RealEstate r JOIN r.devices d WHERE r IN :realEstates")
    List<Device> findDevicesByRealEstates(@Param("realEstates") List<RealEstate> realEstates);

}
