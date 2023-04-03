package com.bsep.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateRevocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true)
    private String userEmail;

    @Column()
    private String userType;

    @Column(unique = true)
    private BigInteger serialNumber;

    @Column()
    private LocalDateTime timestamp;
}
