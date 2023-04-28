package com.bsep.admin.myHouse;

import com.bsep.admin.model.RealEstate;
import com.bsep.admin.model.Role;
import com.bsep.admin.model.User;
import com.bsep.admin.myHouse.dto.RealEstateDto;
import com.bsep.admin.repository.RealEstateRepository;
import com.bsep.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyHouseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RealEstateRepository realEstateRepository;
    public List<RealEstateDto> findRealEstatesForUser(String email) {
        User user = getUser(email);
        return getRealEstateDtoFromRealEstates(user.getRealEstates());

    }

    private List<RealEstateDto> getRealEstateDtoFromRealEstates(List<RealEstate> realEstates) {
        return realEstates.stream().map(realEstate -> new RealEstateDto(realEstate.getId(), realEstate.getAddress(), realEstate.getName())).toList();
    }

    public RealEstateDto addRealEstate(String email, RealEstateDto realEstateDto) {
        User user = getUser(email);
        RealEstate realEstate = new RealEstate(realEstateDto.getAddress(), realEstateDto.getName());
        user.getRealEstates().add(realEstate);
        realEstate = realEstateRepository.save(realEstate);
        userRepository.save(user);
        return new RealEstateDto(realEstate.getId(), realEstate.getAddress(), realEstate.getName());
    }

    private User getUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        if (user.getRole() == Role.ROLE_TENANT || user.getRole() == Role.ROLE_LANDLORD) {
            return user;
        }
        else throw new RuntimeException("User does not have permission to have real estates");
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
        return new RealEstateDto(realEstate.getId(), realEstate.getAddress(), realEstate.getName());
    }
}
