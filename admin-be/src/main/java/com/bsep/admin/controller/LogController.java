package com.bsep.admin.controller;


import com.bsep.admin.dto.LogRuleCreationDto;
import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogAlarm;
import com.bsep.admin.model.LogRule;
import com.bsep.admin.service.LogAlarmService;
import com.bsep.admin.service.LogRulesService;
import com.bsep.admin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogAlarmService logAlarmService;

    @Autowired
    private LogRulesService logRulesService;

    @Autowired
    private LogService logService;

    @GetMapping("/alarms")
    public List<LogAlarm> getAllAlarms() {
        return logAlarmService.getAllAlarms();
    }

    @GetMapping("/rules")
    public List<LogRule> getAllRules() {
        return logRulesService.getAllRules();
    }

    @PostMapping("/rules")
    public void addLogRule(@RequestBody LogRuleCreationDto dto) {
        logRulesService.addRule(dto);
    }

    @DeleteMapping("/rules/{name}")
    public void deleteLogRule(@PathVariable String name) {
        logRulesService.deleteRule(name);
    }

    @GetMapping("/")
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }
}
