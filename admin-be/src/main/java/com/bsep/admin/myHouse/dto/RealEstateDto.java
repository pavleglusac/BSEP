package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.Tenant;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDto {
    private UUID id;
    @NotBlank
    private String address;
    @NotBlank
    private String name;
    private String landlord;

    private List<TenantDto> tenants;
}
