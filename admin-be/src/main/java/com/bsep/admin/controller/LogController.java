package com.bsep.admin.controller;


import com.bsep.admin.dto.LogRuleCreationDto;
import com.bsep.admin.model.Log;
import com.bsep.admin.model.LogAlarm;
import com.bsep.admin.model.LogRule;
import com.bsep.admin.model.LogType;
import com.bsep.admin.myHouse.dto.LogSearchResultDto;
import com.bsep.admin.service.LogAlarmService;
import com.bsep.admin.service.LogRulesService;
import com.bsep.admin.service.LogService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('READ_LOGS')")
    public List<LogAlarm> getAllAlarms() {
        return logAlarmService.getAllAlarms();
    }

    @GetMapping("/rules")
    @PreAuthorize("hasAuthority('READ_LOGS')")
    public List<LogRule> getAllRules() {
        return logRulesService.getAllRules();
    }

    @PostMapping("/rules")
    @PreAuthorize("hasAuthority('WRITE_LOGS')")
    public void addLogRule(@RequestBody LogRuleCreationDto dto) {
        logRulesService.addRule(dto);
    }

    @DeleteMapping("/rules/{name}")
    @PreAuthorize("hasAuthority('WRITE_LOGS')")
    public void deleteLogRule(@PathVariable String name) {
        logRulesService.deleteRule(name);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('READ_LOGS')")
    public LogSearchResultDto searchLogs(
            @RequestParam(required = false, defaultValue = "") String actionQuery,
            @RequestParam(required = false, defaultValue = "") String detailsQuery,
            @RequestParam(required = false, defaultValue = "") String ipAddressQuery,
            @RequestParam(required = false, defaultValue = "") LogType logType,
            @RequestParam(required = false, defaultValue = "") List<String> usernames,
            @RequestParam(required = false, defaultValue = "false") boolean regexEnabled,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(20) int amount
        ) {
        return logService.searchLogs(
                actionQuery, detailsQuery, ipAddressQuery, logType,
                usernames, regexEnabled, page, amount
        );
    }
}
