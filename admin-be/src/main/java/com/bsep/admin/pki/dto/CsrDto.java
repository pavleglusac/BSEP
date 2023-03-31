package com.bsep.admin.pki.dto;

import com.bsep.admin.model.Csr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsrDto extends Csr {
    private String publicKey;

    public CsrDto(Csr csr, String key) {
        super(csr.getId(), csr.getCommonName(), csr.getGivenName(), csr.getSurname(), csr.getOrganization(), csr.getOrganizationalUnit(), csr.getCountry(), csr.getEmail(), csr.getCreatedDate(), csr.getStatus());
        this.publicKey = key;
    }
}
