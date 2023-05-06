package com.bsep.admin.myHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTenantDto {
    private String email;
    private UUID realEstateId;
}
