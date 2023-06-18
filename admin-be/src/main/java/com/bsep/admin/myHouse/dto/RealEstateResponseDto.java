package com.bsep.admin.myHouse.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RealEstateResponseDto {
    private UUID id;
    private String address;
    private String name;
}
