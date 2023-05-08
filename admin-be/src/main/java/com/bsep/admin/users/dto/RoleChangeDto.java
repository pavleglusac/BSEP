package com.bsep.admin.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleChangeDto {
    @NotBlank
    private String newRole;
}
