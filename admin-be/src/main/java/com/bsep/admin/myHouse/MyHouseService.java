package com.bsep.admin.myHouse;

import com.bsep.admin.model.*;
import com.bsep.admin.myHouse.dto.AddTenantDto;
import com.bsep.admin.myHouse.dto.RealEstateDto;
import com.bsep.admin.myHouse.dto.TenantDto;
import com.bsep.admin.repository.LandlordRepository;
import com.bsep.admin.repository.RealEstateRepository;
import com.bsep.admin.repository.TenantRepository;
import com.bsep.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyHouseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LandlordRepository landlordRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private RealEstateRepository realEstateRepository;

    public List<RealEstateDto> findRealEstatesForUser(String email) {
        User user = getUser(email);
        return getRealEstateDtoFromRealEstates(user.getRealEstates());

    }

    private List<RealEstateDto> getRealEstateDtoFromRealEstates(List<RealEstate> realEstates) {
        return realEstates.stream().map(realEstate -> new RealEstateDto(realEstate.getId(), realEstate.getAddress(), realEstate.getName(), getLandlordByRealEstate(realEstate), getTenantsDtoByRealEstate(realEstate), realEstate.getDevices())).toList();
    }

    private String getLandlordByRealEstate(RealEstate realEstate) {
        return landlordRepository.findByRealEstatesContains(realEstate).getEmail();
    }

    private List<TenantDto> getTenantsDtoByRealEstate(RealEstate realEstate) {
        List<Tenant> tenants = this.getTenantsByRealEstate(realEstate);
        return tenants.stream().map(tenant -> new TenantDto(tenant.getId(), tenant.getEmail(), tenant.getName(), tenant.getImageUrl())).toList();
    }

    private List<Tenant> getTenantsByRealEstate(RealEstate realEstate) {
        return tenantRepository.findByRealEstate(realEstate);
    }

    public void removeRealEstatesForLandlord(List<RealEstate> realEstates) {
        List<RealEstate> realEstateCopy = new ArrayList<>(realEstates);
        for (RealEstate realEstate: realEstateCopy) {
            Landlord landlord = landlordRepository.findByRealEstatesContains(realEstate);
            landlord.getRealEstates().remove(realEstate);
            landlordRepository.save(landlord);
            List<Tenant> tenants = this.getTenantsByRealEstate(realEstate);
            tenants.forEach(tenant -> tenant.setRealEstate(null));
            tenantRepository.saveAll(tenants);
            realEstateRepository.deleteById(realEstate.getId());
        }

    }

    public RealEstateDto addRealEstate(String email, RealEstateDto realEstateDto) {
        User user = getUser(email);
        if (!user.hasRole("ROLE_LANDLORD")) {
            throw new RuntimeException("User must be landlord in order to add new real estate.");
        }
        Landlord landlord = (Landlord) user;
        RealEstate realEstate = new RealEstate(realEstateDto.getAddress(), realEstateDto.getName());
        realEstate.setDevices(new ArrayList<>());
        landlord.getRealEstates().add(realEstate);
        realEstate = realEstateRepository.save(realEstate);
        userRepository.save(user);
        return new RealEstateDto(realEstate.getId(), realEstate.getAddress(), realEstate.getName(), email, new ArrayList<>(), new ArrayList<>());
    }

    private User getUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmailVerified()) {
            throw new RuntimeException("User does not have permission to have real estates");
        }

        if (user.hasRole("ROLE_TENANT") || user.hasRole("ROLE_LANDLORD")) {
            return user;
        }
        throw new RuntimeException("User does not have permission to have real estates");
    }

    public RealEstateDto editRealEstate(String email, RealEstateDto realEstateDto) {
        User user = getUser(email);
        Optional<RealEstate> realEstateOpt = realEstateRepository.findById(realEstateDto.getId());
        if (realEstateOpt.isEmpty()) {
            throw new RuntimeException("Real estate not found");
        }
        RealEstate realEstate = realEstateOpt.get();
        if (!user.getRealEstates().contains(realEstate)) {
            throw new RuntimeException("User does not have permission to edit this real estate");
        }
        realEstate.setAddress(realEstateDto.getAddress());
        realEstate.setName(realEstateDto.getName());
        realEstate = realEstateRepository.save(realEstate);
        return new RealEstateDto(realEstate.getId(), realEstate.getAddress(), realEstate.getName(), realEstateDto.getLandlord(), realEstateDto.getTenants(), realEstate.getDevices());
    }

    public TenantDto addTenant(AddTenantDto addTenantDto) {
        User user = getUser(addTenantDto.getEmail());
        if (!user.hasRole("ROLE_TENANT") || !user.getEmailVerified())
            throw new RuntimeException("User does not have permission to be tenant for real estate.");
        Tenant tenant = (Tenant) user;
        Optional<RealEstate> realEstateOpt = realEstateRepository.findById(addTenantDto.getRealEstateId());
        RealEstate realEstate = realEstateOpt.orElseThrow(() -> new RuntimeException("Real estate does not exist"));
        tenant.setRealEstate(realEstate);
        tenantRepository.save(tenant);
        return new TenantDto(user.getId(), user.getEmail(), user.getName(), user.getImageUrl());
    }
}
