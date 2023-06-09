package com.bsep.admin.myHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    String houseId;
    String name;
    String type;
    String filterRegex;
    Long refreshRate;
}
