package com.bsep.admin.users.dto;

import com.bsep.admin.model.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDisplayDto {
    private UUID id;
    private String name;
    private String email;
    private boolean emailVerified;
    private String imageUrl;
    private Role role;
    private boolean isLocked;
}
