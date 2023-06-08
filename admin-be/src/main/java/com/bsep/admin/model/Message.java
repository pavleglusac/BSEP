package com.bsep.admin.model;

import jakarta.persistence.Entity;
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
@Document("messages")
public class Message {

    @Id
    private UUID id;
    private String type;
    private String text;
    private Double value;
    private DeviceType deviceType;
    private LocalDateTime timestamp;

    private UUID deviceId;
    private Boolean read = false;

}
