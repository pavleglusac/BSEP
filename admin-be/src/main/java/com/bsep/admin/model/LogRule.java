package com.bsep.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "log_rules")
public class LogRule {
    @Id
    private UUID id;
    private String name;
    private String alarmText;

    private String logType;
    @Column(name = "action_regex")
    private String actionRegex;
    @Column(name = "details_regex")
    private String detailsRegex;
    @Column(name = "ip_address_regex")
    private String ipAddressRegex;
    @ElementCollection
    private List<String> usernames;
    private Integer num;
    private String operatorNum;
    @Column(name = "window_field")
    private String window;
}
