package com.bsep.admin.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("log_alarms")
public class LogAlarm {
    @Id
    private UUID id;
    private String name;
    private String text;
    private LocalDateTime timestamp;
    private List<Log> logs;
}
