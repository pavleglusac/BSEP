package com.bsep.admin.users;

import com.bsep.admin.exception.InvalidRoleException;
import com.bsep.admin.model.Role;
import com.bsep.admin.model.User;
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

@Service

public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    public Page<UserDisplayDto> search(String query, int page, int amount, List<String> roles, boolean onlyLocked) {
        Set<Role> roleSet = new HashSet<>();
//        EnumSet<Role> roleSet = EnumSet.noneOf(Role.class);
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
            dto.setRole(user.getRoles().get(0));
            dto.setEmailVerified(user.getEmailVerified());
            dto.setImageUrl(user.getImageUrl());
            dto.setLocked(!user.isAccountNonLocked());
            userDisplayDtos.add(dto);
        }
        return new PageImpl<>(userDisplayDtos, userPage.getPageable(), userPage.getTotalElements());
    }

    public void delete(UUID id) {
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
        if (!Objects.equals(role.getName(), "ROLE_ADMIN")) {
            user.getRoles().clear();
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            throw new InvalidRoleException();
        }
    }
}
