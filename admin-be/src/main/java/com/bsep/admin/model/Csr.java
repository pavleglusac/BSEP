package com.bsep.admin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Data
@Table(name = "CSR")
@NoArgsConstructor
public class Csr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column()
    private String commonName;

    @Column()
    private String givenName;

    @Column()
    private String surname;

    @Column()
    private String organization;

    @Column()
    private String organizationalUnit;

    @Column()
    private String country;

    @Column(unique = true, nullable = false)
    private String email;

    @Column()
    private LocalDateTime createdDate;

    @Column(name = "STATUS", nullable = false)
    private CsrStatus status = CsrStatus.PENDING;
}
