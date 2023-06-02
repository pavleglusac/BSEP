package com.bsep.admin.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("alarms")
public class Alarm {
    @Id
    private UUID id;
    private String name;
    private String text;
    private DeviceType deviceType;
    private LocalDateTime timestamp;
    private String deviceId;
}
