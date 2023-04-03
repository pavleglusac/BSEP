package com.bsep.admin.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {
	private BigInteger serialNumber;
	private String algorithm;
	private LocalDateTime validityStart;
	private LocalDateTime validityEnd;
	private List<CertificateOptionDto> extensions;

	private String csrId;

	private Integer hierarchyLevel = 3;

	private CsrDto csrDto;

	private Boolean isRevoked;

	@Override
	public String toString() {
		return "CertificateDto [algorithm=" + algorithm + ", validityStart=" + validityStart + ", validityEnd="
				+ validityEnd + ", extensions=" + extensions + "]";
	}
}
