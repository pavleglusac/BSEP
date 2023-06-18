package com.bsep.admin.pki.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateOptionDto {
	@NotBlank
	private String name;
	private List<CertificateOptionDto> options;
	@NotBlank
	private String value;
	@NotBlank
	private String type;
}
