package com.bsep.admin.myHouse;

import com.bsep.admin.model.Alarm;
import com.bsep.admin.myHouse.dto.Rule;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void addRule(@RequestBody Rule rule) {
        rulesService.addRule(rule);
    }


    @DeleteMapping("/rule")
    public void deleteRule(@RequestParam String ruleName) {
        rulesService.deleteRule(ruleName);
    }


    @GetMapping("/rule")
    public List<Rule> getRules() {
        return rulesService.getAllRules();
    }


    @GetMapping("")
    public List<Alarm> getAlarmForRealEstate(@RequestParam String realEstateId) {
        return alarmService.getAlarmsForRealEstate(UUID.fromString(realEstateId));
    }

    @GetMapping("/device")
    public List<Alarm> getAlarmForDevice(@RequestParam String deviceId) {
        return alarmService.getAlarmsForDevice(UUID.fromString(deviceId));
    }

}
