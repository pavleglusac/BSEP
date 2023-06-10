package com.bsep.admin.myHouse;

import com.bsep.admin.exception.DeviceNotFoundException;
import com.bsep.admin.model.Device;
import com.bsep.admin.model.DeviceType;
import com.bsep.admin.model.Message;
import com.bsep.admin.model.RealEstate;
import com.bsep.admin.myHouse.dto.DeviceDto;
import com.bsep.admin.repository.DeviceRepository;
import com.bsep.admin.repository.MessageRepository;
import com.bsep.admin.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;


    @Autowired
    private DeviceMessageService deviceMessageService;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Device addDevice(DeviceDto deviceDto) {
        UUID houseId = UUID.fromString(deviceDto.getHouseId());
        RealEstate realEstate = realEstateRepository.findById(houseId).orElseThrow(() -> new RuntimeException("Real estate not found"));
        Device device = new Device();
        device.setId(UUID.randomUUID());
        device.setName(deviceDto.getName());
        device.setType(DeviceType.valueOf(deviceDto.getType()));
        device.setFilterRegex(deviceDto.getFilterRegex());
        device.setRefreshRate(deviceDto.getRefreshRate());
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

    public Device findDeviceById(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException("Device not found"));
    }

    public Page<Message> findMessagesForDevice(int page, int amount, UUID id, String type, String text, String value, String timestamp) {
        Device device = findDeviceById(id);
        Integer valueFrom = null;
        Integer valueTo = null;
        LocalDateTime timestampFrom = null;
        LocalDateTime timestampTo = null;
        if (!value.equals("")) {
            valueFrom = Integer.parseInt(value.split(";")[0]);
            valueTo = Integer.parseInt(value.split(";")[1]);
        }
        if (!timestamp.equals("")) {
            timestampFrom = LocalDateTime.parse(timestamp.split(";")[0]);
            timestampTo = LocalDateTime.parse(timestamp.split(";")[1]);
        }
        return messageRepository.findFilteredMessages(id, type, text, valueFrom, valueTo, timestampFrom, timestampTo,PageRequest.of(page, amount, Sort.by(Sort.Direction.DESC, "timestamp")));
    }

}
