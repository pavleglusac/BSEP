package com.bsep.admin.repository;

import com.bsep.admin.model.Landlord;
import com.bsep.admin.model.RealEstate;
import com.bsep.admin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface LandlordRepository extends JpaRepository<User, UUID> {

    Landlord findByRealEstatesContains(RealEstate realEstate);
}
