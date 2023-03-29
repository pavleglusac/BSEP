package com.bsep.admin.controller;

import com.bsep.admin.model.User;
import com.bsep.admin.pki.dto.CertificateDto;
import com.bsep.admin.pki.service.CertificateService;
import com.bsep.admin.pki.service.CsrService;
import org.bouncycastle.operator.OperatorCreationException;
import com.bsep.admin.model.Csr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.cert.CertificateException;

@RestController
@RequestMapping("/api/pki")
public class PkiController {

	@Autowired
	CsrService csrService;

	@Autowired
	CertificateService certificateService;

	// health endpoint
	@GetMapping("/health")
	public String health() {
		return "OK";
	}

	@PostMapping("/csr")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<String> createCsr(@RequestBody Csr csr, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		try{
			csrService.processCsr(csr, user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create CSR");
		}
		return ResponseEntity.ok("CSR created");
	}

	@PostMapping("/certificate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> createCertificate(@RequestBody CertificateDto cert, Authentication authentication) throws CertificateException, OperatorCreationException {
		System.out.println(cert);
		certificateService.processCertificate(cert);
		return ResponseEntity.ok("Certificate created");
	}

}
