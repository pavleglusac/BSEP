package com.bsep.admin.myHouse;

//import io.swagger.v3.oas.annotations.servers.Server;
import com.bsep.admin.exception.RealEstateNotFoundException;
import com.bsep.admin.model.*;
import com.bsep.admin.myHouse.dto.AlarmResponseDto;
import com.bsep.admin.myHouse.dto.DeviceResponseDto;
import com.bsep.admin.myHouse.dto.MessageDto;
import com.bsep.admin.myHouse.dto.RealEstateResponseDto;
import com.bsep.admin.repository.*;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private LogAlarmRepository logAlarmRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RealEstateRepository realEstateRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private LandlordRepository landlordRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TenantRepository tenantRepository;

    public void createAlarm(String name, String text, UUID deviceId) {
        System.err.println("Creating alarm: " + name);
        // TODO: Send alarm to frontend via websocket
        DeviceType deviceType = deviceRepository.findById(deviceId).get().getType();
        Alarm alarm = new Alarm(UUID.randomUUID(), name, text, deviceType, LocalDateTime.now(), deviceId.toString());
        alarmRepository.save(alarm);
        template.convertAndSendToUser("admin@homeguard.com", "/queue/alarms", alarm);
        notifyTenantAndLandlord(alarm);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyTenantAndLandlord(Alarm alarm) {
        Device device = deviceRepository.findById(UUID.fromString(alarm.getDeviceId())).get();
        RealEstate realEstate = realEstateRepository.findByDevice(device);
        if (realEstate == null) {
            throw new RealEstateNotFoundException("Real estate not found for device: " + device.getId() + ".");
        }
        List<Device> devices = new ArrayList<>();
        devices.add(device);
        List<RealEstate> realEstates = new ArrayList<>();
        realEstates.add(realEstate);
        Landlord landlord = landlordRepository.findByRealEstatesContains(realEstate);
        List<Tenant> tenants = tenantRepository.findByRealEstate(realEstate);

        if (landlord != null) {
            template.convertAndSendToUser(landlord.getEmail(), "/queue/alarms", alarm);
        }

        for (Tenant tenant : tenants) {
            template.convertAndSendToUser(tenant.getEmail(), "/queue/alarms", alarm);
        }

    }

    public Page<AlarmResponseDto> getAllAlarms(int page, int amount) {
        Page<Alarm> alarmPage = alarmRepository.findAll(PageRequest.of(page, amount, Sort.by(Sort.Direction.DESC, "timestamp")));
        List<UUID> deviceIds = alarmPage.getContent()
                .stream().map(x -> UUID.fromString(x.getDeviceId())).toList();
        List<Device> devices = deviceRepository.findByIdIn(deviceIds);
        List<RealEstate> realEstates = realEstateRepository.findByDevicesInList(devices);
        return convertAlarmsToAlarmDtos(alarmPage, devices, realEstates);
    }

    public Page<LogAlarm> getAllLogAlarms(int page, int amount) {
        return logAlarmRepository.findAll(PageRequest.of(page, amount, Sort.by(Sort.Direction.DESC, "timestamp")));
    }

    private List<RealEstate> getRealEstateByUser(User user) {
        List<RealEstate> realEstates = new ArrayList<>();
        if (user instanceof Landlord) {
            realEstates = landlordRepository.getRealEstatesByLandlord(user.getId());
        } else if (user instanceof Tenant) {
            realEstates = tenantRepository.getRealEstatesByTenant(user.getId());
        }
        return realEstates;
    }

    public Page<AlarmResponseDto> getAllMineAlarms(User user, int page, int amount) {
        List<RealEstate> realEstates = getRealEstateByUser(user);
        List<Device> devices = realEstateRepository.findDevicesByRealEstates(realEstates);
        List<String> deviceIds = devices.stream().map(x -> x.getId().toString()).toList();
        Page<Alarm> alarmPage = alarmRepository.findAllByDeviceIdIn(deviceIds , PageRequest.of(page, amount));
        return convertAlarmsToAlarmDtos(alarmPage, devices, realEstates);
    }

    public Page<MessageDto> findMessageAlarms(User user, int page, int amount) {
        List<RealEstate> realEstates = getRealEstateByUser(user);
        List<Device> devices = realEstateRepository.findDevicesByRealEstates(realEstates);
        List<UUID> deviceIds = devices.stream().map(Device::getId).toList();
        String type = "ALARM";
        Page<Message> messagePage = messageRepository.findFilteredMessages(deviceIds, type,PageRequest.of(page, amount, Sort.by(Sort.Direction.DESC, "timestamp")));
        return convertMessagesToMessageDtos(messagePage, devices, realEstates);
    }

    private Page<AlarmResponseDto> convertAlarmsToAlarmDtos(Page<Alarm> alarmPage, List<Device> devices, List<RealEstate> realEstates) {
        List<AlarmResponseDto> alarmResponseDtos = new ArrayList<>();
        for (Alarm alarm : alarmPage.getContent()) {
            AlarmResponseDto dto = getAlarmResponseDto(devices, realEstates, alarm);
            alarmResponseDtos.add(dto);
        }
        return new PageImpl<>(alarmResponseDtos, alarmPage.getPageable(), alarmPage.getTotalElements());
    }

    private Page<MessageDto> convertMessagesToMessageDtos(Page<Message> messagePage, List<Device> devices, List<RealEstate> realEstates) {
        List<MessageDto> messageDtos = new ArrayList<>();
        for (Message message : messagePage.getContent()) {
            MessageDto dto = getMessageDto(devices, realEstates, message);
            messageDtos.add(dto);
        }
        return new PageImpl<>(messageDtos, messagePage.getPageable(), messagePage.getTotalElements());
    }

    private AlarmResponseDto getAlarmResponseDto(List<Device> devices, List<RealEstate> realEstates, Alarm alarm) {
        AlarmResponseDto dto = modelMapper.map(alarm, AlarmResponseDto.class);
        Device device = devices.stream()
                .filter(d -> d.getId().equals(UUID.fromString(alarm.getDeviceId())))
                .findFirst().get();
        dto.setDevice(modelMapper.map(device, DeviceResponseDto.class));
        RealEstate realEstate = realEstates.stream()
                .filter(re -> re.getDevices().contains(device))
                .findFirst().get();
        dto.setRealEstate(modelMapper.map(realEstate, RealEstateResponseDto.class));
        return dto;
    }

    private MessageDto getMessageDto(List<Device> devices, List<RealEstate> realEstates, Message message) {
        MessageDto dto = modelMapper.map(message, MessageDto.class);
        Device device = devices.stream()
                .filter(d -> d.getId().equals(message.getDeviceId()))
                .findFirst().get();
        dto.setDevice(modelMapper.map(device, DeviceResponseDto.class));
        RealEstate realEstate = realEstates.stream()
                .filter(re -> re.getDevices().contains(device))
                .findFirst().get();
        dto.setRealEstate(modelMapper.map(realEstate, RealEstateResponseDto.class));
        return dto;
    }

    public List<Alarm> getAlarmsForDevice(UUID deviceId) {
        return alarmRepository.findAllByDeviceId(deviceId);
    }

    public List<Alarm> getAlarmsForRealEstate(UUID realEstateId) {
        // get devices for real estate
        // get alarms for devices
        // return alarms
        List<Device> devices = realEstateRepository.findById(realEstateId).orElseThrow(() -> new RealEstateNotFoundException("Real estate not found")).getDevices();

        return alarmRepository.findAllByDeviceIdIn(devices.stream().map(Device::getId).toList());

    }

    public void notifyTenantAndLandlord(Message message, Device device) {
        RealEstate realEstate = realEstateRepository.findByDevice(device);
        if (realEstate == null) {
            throw new RealEstateNotFoundException("Real estate not found for device: " + device.getId() + ".");
        }

        Landlord landlord = landlordRepository.findByRealEstatesContains(realEstate);
        List<Tenant> tenants = tenantRepository.findByRealEstate(realEstate);

        if (landlord != null) {
            template.convertAndSendToUser(landlord.getEmail(), "/queue/message", message);
        }

        for (Tenant tenant : tenants) {
            template.convertAndSendToUser(tenant.getEmail(), "/queue/message", message);
        }

    }
}
