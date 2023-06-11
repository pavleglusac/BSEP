package com.bsep.admin.myHouse;

import com.bsep.admin.model.Device;
import com.bsep.admin.model.Message;
import com.bsep.admin.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeviceMessageService {

    @Autowired
    private DeviceManagerService deviceManagerService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RulesService rulesService;
    public ConcurrentHashMap<Integer, ArrayList<Device>> timeDevices = new ConcurrentHashMap<>();
    private HashMap<Device, Long> deviceFilePositions = new HashMap<>();

    private int secondsPassed = 1;

    @Scheduled(fixedRate = 1000)
    public void readDeviceData() {
//        System.out.println("Reading device data!!!");
        timeDevices.forEach((key, value) -> {
            if (secondsPassed % key == 0) {
                value.forEach(this::readMessagesFromFile);
            }
        });
        secondsPassed++;
    }


    public void readMessagesFromFile(Device device) {
        String path = device.getFilePath();
        // if file doesn't exist
        if (!Files.exists(Path.of(path))) {
            return;
        }
        try (RandomAccessFile file = new RandomAccessFile(path, "r")) {
            long lastPos = deviceFilePositions.getOrDefault(device, 0L);
            file.seek(lastPos);
            String line;
            Integer msgCount = 0;
            while ((line = file.readLine()) != null) {
                parseMessageLine(device, line);
                msgCount++;
            }
//            System.out.println("Read " + msgCount + " messages from file!!!");
            deviceFilePositions.put(device, file.getFilePointer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseMessageLine(Device device, String line) {
        String[] parts = line.split(",");
        UUID id = UUID.fromString(parts[0]);
        String msgType = parts[1];
        String text = parts[2];
        Double value = Double.parseDouble(parts[3]);
        LocalDateTime timestamp = LocalDateTime.parse(parts[4]);

        // if text matches regex
        if (true) {
            Message message = new Message(id, msgType, text, value, device.getType(), timestamp, device.getId(), false);
            messageRepository.save(message);
            rulesService.addMessage(message);
        }


    }

    public void addDevice(Device device) {
        if (device.getRefreshRate() != null) {
            timeDevices.computeIfAbsent(device.getRefreshRate().intValue(), k -> new ArrayList<>()).add(device);
            deviceManagerService.startDevice(device);
        } else {
            throw new RuntimeException("Couldn't add device");
        }
    }

    public void removeDevice(Device device) {
        timeDevices.computeIfAbsent(device.getRefreshRate().intValue(), k -> new ArrayList<>()).remove(device);
        deviceManagerService.stopDevice(device);
    }

}
