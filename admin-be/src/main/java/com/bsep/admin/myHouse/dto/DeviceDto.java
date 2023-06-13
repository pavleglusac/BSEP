package com.bsep.admin.myHouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    @NotBlank
    String houseId;
    @NotBlank
    String name;
    @NotBlank
    String type;
    @NotBlank
    String filterRegex;
    @Min(1)
    Long refreshRate;
}
