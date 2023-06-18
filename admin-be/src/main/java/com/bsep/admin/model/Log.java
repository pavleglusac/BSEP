package com.bsep.admin.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
@Expires("30d")
@Timestamp("timestamp")
public class Log {
    @Id
    private UUID id;
    private Date timestamp;
    private String action;
    private String details;
    private String ipAddress;
    private List<String> usernames;
    private LogType type;
    private Boolean read;
}
