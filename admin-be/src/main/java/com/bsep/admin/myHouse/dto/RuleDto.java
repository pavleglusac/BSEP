package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleDto {
    private String name;
    private String messageType;
    private String textRegex;
    private DeviceType deviceType;
    private Double value;
    private String operatorValue;
    private Integer num;
    private String operatorNum;
    private String window;
    private String alarmText;
}
