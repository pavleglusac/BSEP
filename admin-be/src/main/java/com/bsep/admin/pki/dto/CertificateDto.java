package com.bsep.admin.pki.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
	@NotBlank
	private String algorithm;
	private LocalDateTime validityStart;
	private LocalDateTime validityEnd;
	private List<CertificateOptionDto> extensions;

	@NotBlank
	private String csrId;

	@Min(0)
	@Max(3)
	private Integer hierarchyLevel = 3;

	private CsrDto csr;

	private Boolean isRevoked;

	@Override
	public String toString() {
		return "CertificateDto [algorithm=" + algorithm + ", validityStart=" + validityStart + ", validityEnd="
				+ validityEnd + ", extensions=" + extensions + "]";
	}
}
