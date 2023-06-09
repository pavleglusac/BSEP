package com.bsep.admin.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    private UUID id;
    private LocalDateTime timestamp;
    private String action;
    private String details;
    private String ipAddress;
    private List<String> usernames;
    private LogType type;
    private Boolean read;
}
