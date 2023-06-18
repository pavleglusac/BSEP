package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MessageDto {
    private UUID id;
    private String type;
    private String text;
    private Double value;
    private DeviceType deviceType;
    private Date timestamp;
    private DeviceResponseDto device;
    private RealEstateResponseDto realEstate;
}
