package com.bsep.admin.repository;

import com.bsep.admin.model.Landlord;
import com.bsep.admin.model.RealEstate;
import com.bsep.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LandlordRepository extends JpaRepository<User, UUID> {

    Landlord findByRealEstatesContains(RealEstate realEstate);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END FROM Landlord u JOIN u.realEstates re JOIN re.devices d WHERE u.id = :userId AND d.id = :deviceId")
    boolean doesUserHaveDevice(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);

    //get real estates by landlord
    @Query("SELECT re FROM Landlord u JOIN u.realEstates re WHERE u.id = :userId")
    List<RealEstate> getRealEstatesByLandlord(@Param("userId") UUID userId);

}
