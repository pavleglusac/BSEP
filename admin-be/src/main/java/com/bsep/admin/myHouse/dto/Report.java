package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    DeviceType deviceType;
    String deviceName;
    int numberOfAlarmMessages;
    int numberOfInfoMessages;
    int numberOfMessages;
    Double minValue;
    Double maxValue;
    Double averageValue;
}
