package com.bsep.admin.model;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends User{

    @ManyToOne()
    private RealEstate realEstate;

    public Tenant(User user) {
        super(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), user.getLoginAttempts(), user.getPassword(), user.getDeleted(), user.getLoginToken(), user.getEmailVerified(), user.getEmailVerificationToken(), user.getRoles());
    }
    @Override
    public List<RealEstate> getRealEstates() {
        List<RealEstate> realEstates = new ArrayList<>();
        if (this.realEstate != null)
            realEstates.add(this.realEstate);
        return realEstates;
    }
}
