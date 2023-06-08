package com.bsep.admin.myHouse;

//import io.swagger.v3.oas.annotations.servers.Server;
import com.bsep.admin.model.Alarm;
import com.bsep.admin.model.Device;
import com.bsep.admin.model.DeviceType;
import com.bsep.admin.repository.AlarmRepository;
import com.bsep.admin.repository.DeviceRepository;
import com.bsep.admin.repository.RealEstateRepository;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    public void createAlarm(String name, String text, UUID deviceId) {
        System.err.println("Creating alarm: " + name);
        // TODO: Send alarm to frontend via websocket
        DeviceType deviceType = deviceRepository.findById(deviceId).get().getType();
        Alarm alarm = new Alarm(UUID.randomUUID(), name, text, deviceType, LocalDateTime.now(), deviceId.toString());
        alarmRepository.save(alarm);
    }

    public List<Alarm> getAllAlarms() {
        return alarmRepository.findAll();
    }

    public List<Alarm> getAlarmsForDevice(UUID deviceId) {
        return alarmRepository.findAllByDeviceId(deviceId);
    }

    public List<Alarm> getAlarmsForRealEstate(UUID realEstateId) {
        // get devices for real estate
        // get alarms for devices
        // return alarms
        List<Device> devices = realEstateRepository.findById(realEstateId).orElseThrow(() -> new RuntimeException("Real estate not found")).getDevices();

        return alarmRepository.findAllByDeviceIdIn(devices.stream().map(Device::getId).toList());

    }

}
