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
    @NotBlank
    @Length(max=256)
    private String textRegex;
    @NotBlank
    private DeviceType deviceType;
    @NotNull
    private Double value;
    @NotBlank
    @Pattern(regexp = "^(?:==|>=|<=|>|<)$")
    private String operatorValue;
    @NotNull
    private Integer num;
    @NotBlank
    @Pattern(regexp = "^(?:==|>=|<=|>|<)$")
    private String operatorNum;
    @NotBlank
    @Length(max=32)
    private String window;
    @NotBlank
    @Length(max=256)
    @Pattern(regexp = "^[\\w.!]+(?: [\\w.!]+)*$")
    private String alarmText;
}
