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

    @Override
    public List<RealEstate> getRealEstates() {
        List<RealEstate> realEstates = new ArrayList<>();
        if (this.realEstate != null)
            realEstates.add(this.realEstate);
        return realEstates;
    }
}
