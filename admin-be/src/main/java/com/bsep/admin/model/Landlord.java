package com.bsep.admin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Landlord extends User{

    @OneToMany()
    private List<RealEstate> realEstates;

    public Landlord(User user) {
        super(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), user.getLoginAttempts(), user.getPassword(), user.getDeleted(), user.getLoginToken(), user.getEmailVerified(), user.getEmailVerificationToken(), user.getRoles());
        this.realEstates = new ArrayList<>();
    }
}
