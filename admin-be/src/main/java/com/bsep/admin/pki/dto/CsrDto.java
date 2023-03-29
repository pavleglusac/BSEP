package com.bsep.admin.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsrDto {
    private String commonName;
    private String givenName;
    private String surname;
    private String organization;
    private String organizationalUnit;
    private String country;
    private String email;
}
