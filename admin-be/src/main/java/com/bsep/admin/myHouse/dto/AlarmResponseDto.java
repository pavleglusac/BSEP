package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AlarmResponseDto {
    private UUID id;
    private String name;
    private String text;
    private DeviceType deviceType;
    private LocalDateTime timestamp;
    private DeviceResponseDto device;
    private RealEstateResponseDto realEstate;

}
