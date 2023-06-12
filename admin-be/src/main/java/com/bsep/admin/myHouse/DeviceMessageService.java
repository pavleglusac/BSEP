package com.bsep.admin.myHouse;

import com.bsep.admin.keystores.KeyStoreReader;
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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.time.LocalDateTime;
import java.util.*;
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



    public Boolean verifyHash(String hash, String text) {
        try {
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            PublicKey publicKey = keyStoreReader.readPublicKey("./keystores/admin.jks", "admin", "bebenem");
            byte[] bytesOfHash = Base64.getDecoder().decode(hash);
            byte[] bytesOfText = text.getBytes("UTF-8");
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(bytesOfText);
            return signature.verify(bytesOfHash);
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying the hash", e);
        }
    }


    private void parseMessageLine(Device device, String line) {
        line = line.strip();
        line = line.trim();
        String[] parts = line.split(",");
        UUID id = UUID.fromString(parts[0]);
        String msgType = parts[1];
        String text = parts[2];
        Double value = Double.parseDouble(parts[3]);
        LocalDateTime timestamp = LocalDateTime.parse(parts[4]);

        String hash = parts[5].trim().strip();
        String noHash = String.join(",", parts[0], parts[1], parts[2], parts[3], parts[4]);

        if (!verifyHash(hash, noHash)) {
            return;
        }

        System.out.println("Verified hash!!!");


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
