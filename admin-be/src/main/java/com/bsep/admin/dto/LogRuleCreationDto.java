package com.bsep.admin.dto;

import com.bsep.admin.model.LogType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class LogRuleCreationDto {

    @NotBlank
    @Length(min=2, max=48)
    @Pattern(regexp = "^\\w+(?: \\w+)*$")
    private String name;
    @NotBlank
    @Length(max=256)
    @Pattern(regexp = "^[\\w.!]+(?: [\\w.!]+)*$")
    private String alarmText;
    private LogType logType;
    @Length(max=256)
    private String actionRegex;
    @Length(max=256)
    private String detailsRegex;
    @Length(max=256)
    private String ipAddressRegex;
    @Size(max = 128)
    private List<String> usernames;
    @NotNull
    private Integer num;
    @NotBlank
    @Pattern(regexp = "^(?:==|>=|<=|>|<)$")
    private String operatorNum;
    @Length(max=32)
    private String window;
}
