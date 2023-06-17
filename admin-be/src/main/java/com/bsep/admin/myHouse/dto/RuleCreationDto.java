package com.bsep.admin.myHouse.dto;

import com.bsep.admin.model.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RuleCreationDto {
    @NotBlank
    @Length(min=2, max=48)
    @Pattern(regexp = "^\\w+(?: \\w+)*$")
    private String name;
    @NotBlank
    @Length(max=32)
    @Pattern(regexp = "^\\w+(?: \\w+)*$")
    private String messageType;
    @Length(max=256)
    private String textRegex;
    private DeviceType deviceType;
    @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$")
    private String deviceId;
    private Double value;
    @Pattern(regexp = "^(?:==|>=|<=|>|<)$")
    private String operatorValue;
    @NotNull
    private Integer num;
    @NotBlank
    @Pattern(regexp = "^(?:==|>=|<=|>|<)$")
    private String operatorNum;
    @Length(max=32)
    private String window;
    @NotBlank
    @Length(max=256)
    @Pattern(regexp = "^[\\w.!]+(?: [\\w.!]+)*$")
    private String alarmText;
}
