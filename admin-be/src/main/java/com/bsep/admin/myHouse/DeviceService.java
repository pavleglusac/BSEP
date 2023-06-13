package com.bsep.admin.myHouse;

import com.bsep.admin.exception.DeviceNotFoundException;
import com.bsep.admin.exception.RealEstateNotFoundException;
import com.bsep.admin.model.Device;
import com.bsep.admin.model.DeviceType;
import com.bsep.admin.model.Message;
import com.bsep.admin.model.RealEstate;
import com.bsep.admin.myHouse.dto.DeviceDto;
import com.bsep.admin.myHouse.dto.Report;
import com.bsep.admin.repository.DeviceRepository;
import com.bsep.admin.repository.MessageRepository;
import com.bsep.admin.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        RealEstate realEstate = realEstateRepository.findById(houseId).orElseThrow(() -> new RealEstateNotFoundException("Real estate not found"));
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
        Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        deviceRepository.delete(device);
        deviceMessageService.removeDevice(device);
    }

    public Device findDeviceById(UUID id) {
        return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException("Device not found"));
    }

    public Page<Message> findMessagesForDevice(int page, int amount, UUID id, String type, String text, String value, String timestamp) {
        Device device = findDeviceById(id);
        Float valueFrom = null;
        Float valueTo = null;
        LocalDateTime timestampFrom = null;
        LocalDateTime timestampTo = null;
        if (!value.equals("")) {
            valueFrom = Float.parseFloat(value.split(";")[0]);
            valueTo = Float.parseFloat(value.split(";")[1]);
        }
        if (!timestamp.equals("")) {
            LocalDate timestampFromDate = LocalDate.parse(timestamp.split(";")[0]);
            LocalDate timestampToDate = LocalDate.parse(timestamp.split(";")[1]);
            timestampFrom = timestampFromDate.atStartOfDay();
            timestampTo = timestampToDate.atTime(23, 59, 59);
        }
        return messageRepository.findFilteredMessages(id, type, text, valueFrom, valueTo, timestampFrom, timestampTo,PageRequest.of(page, amount, Sort.by(Sort.Direction.DESC, "timestamp")));
    }

    public List<Report> findReportForRealEstate(RealEstate realEstate, LocalDate from, LocalDate to) {
        List<Report> reports = new ArrayList<>();
        List<Device> devices = realEstate.getDevices();
        for (Device device : devices) {
            reports.add(findReportForDevice(device, from, to));
        }
        return reports;
    }

    public Report findReportForDevice(Device device, LocalDate from, LocalDate to) {
        Report report = new Report();
        report.setDeviceName(device.getName());
        report.setDeviceType(device.getType());
        List<Message> messages = this.getMessages(device.getId(), from, to);
        report.setNumberOfMessages(messages.size());
        report.setNumberOfAlarmMessages((int) messages.stream().filter(message -> message.getType().equals("ALARM")).count());
        report.setNumberOfInfoMessages((int) messages.stream().filter(message -> message.getType().equals("INFO")).count());
        report.setMinValue(findMinValueForDevice(messages));
        report.setMaxValue(findMaxValueForDevice(messages));
        report.setAverageValue(findAverageValueForDevice(messages));
        return report;
    }

    private List<Message> getMessages(UUID id, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return messageRepository.findAllByDeviceId(id);
        } else {
            return messageRepository.findAllByDeviceIdAndTimestampBetween(id, from.atStartOfDay(), to.atTime(23, 59, 59));
        }
    }

    public List<Report> findReportForDevice(UUID id, LocalDate from, LocalDate to) {
        List<Report> reports = new ArrayList<>();
        Device device = findDeviceById(id);
        reports.add(findReportForDevice(device, from, to));
        return reports;
    }

    public Double findMaxValueForDevice(List<Message> messages) {
        Double max = 0.0;
        for (Message message : messages) {
            if (message.getValue() > max) {
                max = message.getValue();
            }
        }
        return max;
    }

    public Double findMinValueForDevice(List<Message> messages) {
        Double min = 0.0;
        for (Message message : messages) {
            if (message.getValue() < min) {
                min = message.getValue();
            }
        }
        return min;
    }

    public Double findAverageValueForDevice(List<Message> messages) {
        if (messages.size() == 0) {
            return 0.0;
        }
        Double sum = 0.0;
        for (Message message : messages) {
            sum += message.getValue();
        }
        return sum / messages.size();
    }

}
