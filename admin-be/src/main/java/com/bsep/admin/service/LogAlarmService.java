package com.bsep.admin.service;

import com.bsep.admin.model.*;
import com.bsep.admin.repository.AlarmRepository;
import com.bsep.admin.repository.DeviceRepository;
import com.bsep.admin.repository.LogAlarmRepository;
import com.bsep.admin.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LogAlarmService {

    @Autowired
    private LogAlarmRepository alarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private SimpMessagingTemplate template;

    public void createAlarm(String name, String text, List<Log> logs) {
        System.err.println("Creating alarm: " + name);
        // TODO: Send alarm to frontend via websocket
        // print all logs
        for (Log log : logs) {
            System.err.println(log);
        }
        LogAlarm alarm = new LogAlarm(UUID.randomUUID(), name, text, LocalDateTime.now(), logs);
        alarmRepository.save(alarm);
        template.convertAndSendToUser("admin@homeguard.com", "/queue/logs", alarm);
    }

    public List<LogAlarm> getAllAlarms() {
        return alarmRepository.findAll();
    }

}
