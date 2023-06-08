package com.bsep.admin.myHouse;

import com.bsep.admin.model.Device;
import com.bsep.admin.model.DeviceType;
import com.bsep.admin.model.RealEstate;
import com.bsep.admin.myHouse.dto.DeviceDto;
import com.bsep.admin.repository.DeviceRepository;
import com.bsep.admin.repository.RealEstateRepository;
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

    @Autowired
    private RealEstateRepository realEstateRepository;

    public Device addDevice(DeviceDto deviceDto) {
        UUID houseId = UUID.fromString(deviceDto.getHouseId());
        RealEstate realEstate = realEstateRepository.findById(houseId).orElseThrow(() -> new RuntimeException("Real estate not found"));
        Device device = new Device();
        device.setId(UUID.randomUUID());
        device.setName(deviceDto.getDeviceName());
        device.setType(DeviceType.valueOf(deviceDto.getDeviceType()));
        device.setFilterRegex(deviceDto.getRegex());
        device.setRefreshRate(device.getRefreshRate());
        device.setFilePath("./devices/logs/" + device.getId().toString() + ".log");
        deviceRepository.save(device);
        realEstate.getDevices().add(device);
        realEstateRepository.save(realEstate);
        deviceMessageService.addDevice(device);
        return device;
    }

    public void removeDevice(String deviceId) {
        UUID id = UUID.fromString(deviceId);
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
        deviceRepository.delete(device);
        deviceMessageService.removeDevice(device);
    }


}
