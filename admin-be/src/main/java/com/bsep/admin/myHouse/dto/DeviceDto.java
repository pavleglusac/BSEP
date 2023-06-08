package com.bsep.admin.myHouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {
    String houseId;
    String deviceName;
    String deviceType;
    String regex;
    Long refreshRate;
}
