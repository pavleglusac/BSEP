package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drools_rules")
public class Rule {
    @Id
    private UUID id;
    private String name;
    private String messageType;
    @Column(name = "text_regex")
    private String textRegex;
    private DeviceType deviceType;
    private UUID deviceId;
    @Column(name = "value_field")
    private Double value;
    private String operatorValue;
    private Integer num;
    private String operatorNum;
    @Column(name = "window_field")
    private String window;
    private String alarmText;
}
