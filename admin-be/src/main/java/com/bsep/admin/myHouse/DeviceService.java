package com.bsep.admin.myHouse;

import com.bsep.admin.model.Device;
import com.bsep.admin.model.DeviceType;
import com.bsep.admin.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;


    @Autowired
    private DeviceMessageService deviceMessageService;

    public void addDevice(String deviceType, String regex, Long refreshRate) {
        Device device = new Device();
        device.setId(UUID.randomUUID());
        device.setType(DeviceType.valueOf(deviceType));
        device.setFilterRegex(regex);
        device.setRefreshRate(refreshRate);
        device.setFilePath("./devices/logs/" + device.getId().toString() + ".log");
        deviceRepository.save(device);
        deviceMessageService.addDevice(device);
    }
}
