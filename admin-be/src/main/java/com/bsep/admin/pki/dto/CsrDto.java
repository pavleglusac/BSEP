package com.bsep.admin.pki.dto;

import com.bsep.admin.model.Csr;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsrDto extends Csr {
    @NotBlank
    private String publicKey;

    public CsrDto(Csr csr, String key) {
        super(csr.getId(), csr.getCommonName(), csr.getGivenName(), csr.getSurname(), csr.getOrganization(), csr.getOrganizationalUnit(), csr.getCountry(), csr.getEmail(), csr.getCreationDate(), csr.getStatus());
        this.publicKey = key;
    }
}
