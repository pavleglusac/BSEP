package com.bsep.admin.myHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDto {
    private UUID id;
    private String email;
    private String name;
    private String imageUrl;
}
