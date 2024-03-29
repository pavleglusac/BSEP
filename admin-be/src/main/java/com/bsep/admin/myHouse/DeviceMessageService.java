package com.bsep.admin.myHouse;

import com.bsep.admin.exception.InvalidDeviceException;
import com.bsep.admin.exception.MessageSignatureException;
import com.bsep.admin.keystores.KeyStoreReader;
import com.bsep.admin.model.Device;
import com.bsep.admin.model.Message;
import com.bsep.admin.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
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
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeviceMessageService {

    @Autowired
    private DeviceManagerService deviceManagerService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RulesService rulesService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private AlarmService alarmService;

    public ConcurrentHashMap<Integer, ArrayList<Device>> timeDevices = new ConcurrentHashMap<>();
    private HashMap<Device, Long> deviceFilePositions = new HashMap<>();

    private int secondsPassed = 1;

    @Scheduled(fixedRate = 1000)
    public void readDeviceData() {
//        System.out.println("Reading dev
//        ice data!!!");
        pruneOldMessages();
        timeDevices.forEach((key, value) -> {
            if (secondsPassed % key == 0) {
                value.forEach(this::readMessagesFromFile);
            }
        });
        secondsPassed++;
    }

    @Transactional
    public void pruneOldMessages() {
        messageRepository.keep100MostRecentMessages();
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
            throw new MessageSignatureException("Error while verifying the hash.");
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
        Date timestamp = Date.from(LocalDateTime.parse(parts[4]).atZone(ZoneId.systemDefault()).toInstant());

        String hash = parts[5].trim().strip();
        String noHash = String.join(",", parts[0], parts[1], parts[2], parts[3], parts[4]);

        if (!verifyHash(hash, noHash)) {
            return;
        }

        // if text matches regex
        if (noHash.matches(device.getFilterRegex())) {
            Message message = new Message(id, msgType, text, value, device.getType(), timestamp, device.getId(), false);
            messageRepository.save(message);
            rulesService.addMessage(message);
            sendAlarmIfNeeded(message, device);
        }
    }


    public void sendAlarmIfNeeded(Message message, Device device) {
        if(!message.getType().equals("ALARM")) return;
        alarmService.notifyTenantAndLandlord(message, device);

    }

    public void addDevice(Device device) {
        if (device.getRefreshRate() != null) {
            timeDevices.computeIfAbsent(device.getRefreshRate().intValue(), k -> new ArrayList<>()).add(device);
            deviceManagerService.startDevice(device);
        } else {
            throw new InvalidDeviceException("Couldn't add device");
        }
    }

    public void removeDevice(Device device) {
        timeDevices.computeIfAbsent(device.getRefreshRate().intValue(), k -> new ArrayList<>()).remove(device);
        deviceManagerService.stopDevice(device);
    }

}
