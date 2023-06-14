package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import lombok.Data;

import java.util.UUID;

@Data
public class DeviceResponseDto {
    private UUID id;
    private String name;
    private DeviceType type;
    private Long refreshRate;
    private String filterRegex;
}
