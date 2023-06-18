package com.bsep.admin.myHouse;

import com.bsep.admin.model.Alarm;
import com.bsep.admin.model.LogAlarm;
import com.bsep.admin.model.Message;
import com.bsep.admin.model.User;
import com.bsep.admin.myHouse.dto.AlarmResponseDto;
import com.bsep.admin.myHouse.dto.MessageDto;
import com.bsep.admin.myHouse.dto.Rule;
import com.bsep.admin.myHouse.dto.RuleCreationDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/myhouse/alarm")
public class AlarmController {

    @Autowired
    private RulesService rulesService;


    @Autowired
    private AlarmService alarmService;


    @PostMapping("/rule")
    @PreAuthorize("hasAuthority('WRITE_ALARM')")
    public void addRule(@RequestBody RuleCreationDto rule) {
        rulesService.addRule(rule);
    }


    @DeleteMapping("/rule")
    @PreAuthorize("hasAuthority('WRITE_ALARM')")
    public void deleteRule(@RequestParam String ruleName) {
        rulesService.deleteRule(ruleName);
    }


    @GetMapping("/rule")
    @PreAuthorize("hasAuthority('READ_ALARM')")
    public List<Rule> getRules() {
        return rulesService.getAllRules();
    }

    @GetMapping("")
    public Page<AlarmResponseDto> getAlarms(
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(20) int amount
    ) {
        return alarmService.getAllAlarms(page, amount);
    }

    @GetMapping("/logAlarm")
    public Page<LogAlarm> getLogAlarms(
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(20) int amount
    ) {
        return alarmService.getAllLogAlarms(page, amount);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyAuthority('READ_THREATS')")
    public Page<AlarmResponseDto> getMineAlarms(
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(20) int amount,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return alarmService.getAllMineAlarms(user, page, amount);
    }

    @GetMapping("/mine/messages")
    @PreAuthorize("hasAnyAuthority('READ_THREATS')")
    public ResponseEntity<Page<MessageDto>> findMessageAlarms(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                              @RequestParam(defaultValue = "12") @Min(1) @Max(20) int amount,
                                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(alarmService.findMessageAlarms(user,page, amount));
    }

    @GetMapping("/{realEstateId}")
    @PreAuthorize("hasAuthority('READ_ALARM')")
    public List<Alarm> getAlarmForRealEstate(@PathVariable String realEstateId) {
        return alarmService.getAlarmsForRealEstate(UUID.fromString(realEstateId));
    }

    @GetMapping("/device")
    @PreAuthorize("hasAuthority('READ_ALARM')")
    public List<Alarm> getAlarmForDevice(@RequestParam String deviceId) {
        return alarmService.getAlarmsForDevice(UUID.fromString(deviceId));
    }

}
