package com.bsep.admin.pki.controller;

import com.bsep.admin.model.User;
import com.bsep.admin.pki.dto.CertificateDto;
import com.bsep.admin.pki.dto.CertificateRevocationDto;
import com.bsep.admin.pki.dto.CsrDto;
import com.bsep.admin.pki.service.CsrService;
import com.bsep.admin.service.MailingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.bouncycastle.operator.OperatorCreationException;
import com.bsep.admin.model.Csr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bsep.admin.pki.service.CertificateService;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	@PreAuthorize("hasAuthority('WRITE_CSR')")
	public ResponseEntity<Map<String, String>> createCsr(@RequestBody Csr csr, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		try {
			csrService.processCsr(csr, user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create CSR");
		}
		return ResponseEntity.ok(Map.of("message", "CSR created"));
	}

	@GetMapping("/csr")
	@PreAuthorize("hasAuthority('READ_CSR')")
	public ResponseEntity<List<CsrDto>> findAllCsr() {
		return ResponseEntity.ok(csrService.findAllCsr());
	}

	@GetMapping("/csr/{email}")
	@PreAuthorize("hasAuthority('READ_CSR')")
	public Csr getCsr(@PathVariable @Email String email) {
		return csrService.getCsrByUser(email);
	}

	@PostMapping(value = "/certificate", produces = "application/json")
	@PreAuthorize("hasAuthority('CERTIFICATE_MANAGEMENT')")
	public ResponseEntity<String> createCertificate(@Valid @RequestBody CertificateDto cert, Authentication authentication) throws CertificateException, OperatorCreationException, NoSuchAlgorithmException, KeyStoreException, InvalidKeySpecException {
		certificateService.processCertificate(cert);
		return ResponseEntity.ok("Certificate created");
	}

	@GetMapping("/certificate")
	@PreAuthorize("hasAuthority('CERTIFICATE_MANAGEMENT')")
	public ResponseEntity<List<CertificateDto>> findAllCertificate() {
		return ResponseEntity.ok(certificateService.findAllCertificate());
	}

	@PostMapping(value = "/certificate-revocation", produces = "application/json")
	@PreAuthorize("hasAuthority('CERTIFICATE_MANAGEMENT')")
	public ResponseEntity<String> revokeCertificate(@Valid @RequestBody CertificateRevocationDto dto, Authentication authentication) {
		certificateService.revokeCertificate(dto);
		return ResponseEntity.ok("Certificate revoked");
	}

	@GetMapping("certificate/distribute/{email}")
	@PreAuthorize("hasAuthority('CERTIFICATE_MANAGEMENT')")
	public ResponseEntity<Map<String, String>> distributeCertificate(@PathVariable @Email String email) {
		return ResponseEntity.ok(Map.of("message", certificateService.distributeCertificate(email)));
	}

	@DeleteMapping("/csr/{id}")
	@PreAuthorize("hasAuthority('CERTIFICATE_MANAGEMENT')")
	public ResponseEntity<Map<String, String>> denyCertificate(@PathVariable UUID id) {
		return ResponseEntity.ok(Map.of("message", csrService.denyCsr(id)));
	}

	@Autowired
	private MailingService mailingService;

	@GetMapping("/send/{email}")
	public ResponseEntity<String> sendCertificate(@PathVariable @Email String email) {
//		mailingService.sendTestMail();
		return ResponseEntity.ok("Certificate sent");
	}

	@GetMapping("/validate/{serialNumber}")
	public ResponseEntity<Boolean> validateCertificate(@PathVariable String serialNumber) {
		return ResponseEntity.ok(certificateService.validateCertificate(serialNumber));
	}

}
