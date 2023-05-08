package com.bsep.admin.users;

import com.bsep.admin.exception.InvalidQueryException;
import com.bsep.admin.exception.InvalidRoleException;
import com.bsep.admin.model.*;
import com.bsep.admin.myHouse.MyHouseService;
import com.bsep.admin.repository.RealEstateRepository;
import com.bsep.admin.repository.RoleRepository;
import com.bsep.admin.repository.UserRepository;
import com.bsep.admin.users.dto.RoleChangeDto;
import com.bsep.admin.users.dto.UserDisplayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service

public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public MyHouseService myHouseService;
    private static final Pattern regex = Pattern.compile("^[\\w.@\\s]*$");

    public Page<UserDisplayDto> search(String query, int page, int amount, List<String> roles, boolean onlyLocked) {
        if (!regex.matcher(query).matches()) {
            throw new InvalidQueryException();
        }
        Set<Role> roleSet = new HashSet<>();
        try {
            List<Role> rolesList = roles.stream().map(x -> roleRepository.findByName(x).orElseThrow()).toList();
            roleSet.addAll(rolesList);
        } catch (Exception e) {
            throw new InvalidRoleException();
        }
        Page<User> userPage = userRepository.search(query, roleSet, onlyLocked, PageRequest.of(page, amount));
        return convertUserPageToDtoPage(userPage);
    }

    private Page<UserDisplayDto> convertUserPageToDtoPage(Page<User> userPage) {
        List<UserDisplayDto> userDisplayDtos = new ArrayList<>();
        for (User user : userPage.getContent()) {
            UserDisplayDto dto = new UserDisplayDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRoles().get(0).getName());
            dto.setEmailVerified(user.getEmailVerified());
            dto.setImageUrl(user.getImageUrl());
            dto.setLocked(!user.isAccountNonLocked());
            userDisplayDtos.add(dto);
        }
        return new PageImpl<>(userDisplayDtos, userPage.getPageable(), userPage.getTotalElements());
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        if (Objects.equals(user.getRoles().get(0).getName(), "ROLE_LANDLORD")) {
            myHouseService.removeRealEstatesForLandlord(user.getRealEstates());
        }
        userRepository.deleteById(id);
    }

    public void changeRole(UUID id, RoleChangeDto dto) {
        User user = userRepository.findById(id).orElseThrow();
        Role role;
        try {
            role = roleRepository.findByName(dto.getNewRole()).orElseThrow();
        } catch (Exception e) {
            throw new InvalidRoleException();
        }
        if (Objects.equals(role.getName(), "ROLE_TENANT")) {
            Tenant tenant = new Tenant(user);
            tenant.getRoles().clear();
            tenant.getRoles().add(role);
            myHouseService.removeRealEstatesForLandlord(user.getRealEstates());
            userRepository.deleteById(user.getId());
            userRepository.save(tenant);
        }
        else if (Objects.equals(role.getName(), "ROLE_LANDLORD")) {
            Landlord landlord = new Landlord(user);
            landlord.getRoles().clear();
            landlord.getRoles().add(role);
            userRepository.deleteById(user.getId());
            userRepository.save(landlord);
        }
        else {
            throw new InvalidRoleException();
        }
    }
}
