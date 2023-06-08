package com.bsep.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "DEVICE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public class Device {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    private String filePath;

    private Long refreshRate;

    private String filterRegex;

}
