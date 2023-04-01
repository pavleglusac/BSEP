package com.bsep.admin.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateOptionDto {
	private String name;
	private List<CertificateOptionDto> options;
	private String value;
	private String type;
}
