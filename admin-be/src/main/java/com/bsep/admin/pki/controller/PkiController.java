package com.bsep.admin.pki.controller;

import com.bsep.admin.model.User;
import com.bsep.admin.pki.dto.CertificateDto;
import com.bsep.admin.pki.dto.CertificateRevocationDto;
import com.bsep.admin.pki.dto.CsrDto;
import com.bsep.admin.pki.service.CsrService;
import com.bsep.admin.service.MailingService;
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<CsrDto>> findAllCsr() {
		return ResponseEntity.ok(csrService.findAllCsr());
	}

	@GetMapping("/csr/{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Csr getCsr(@PathVariable String email) {
		return csrService.getCsrByUser(email);
	}

	@PostMapping(value = "/certificate", produces = "application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> createCertificate(@RequestBody CertificateDto cert, Authentication authentication) throws CertificateException, OperatorCreationException, NoSuchAlgorithmException, KeyStoreException, InvalidKeySpecException {
		certificateService.processCertificate(cert);
		return ResponseEntity.ok("Certificate created");
	}

	@GetMapping("/certificate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<CertificateDto>> findAllCertificate() {
		return ResponseEntity.ok(certificateService.findAllCertificate());
	}

	@PostMapping(value = "/certificate-revocation", produces = "application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> revokeCertificate(@RequestBody CertificateRevocationDto dto, Authentication authentication) {
		certificateService.revokeCertificate(dto);
		return ResponseEntity.ok("Certificate revoked");
	}

	@GetMapping("/distribute/{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> distributeCertificate(@PathVariable String email) {
		return ResponseEntity.ok(certificateService.distributeCertificate(email));
	}

	@Autowired
	private MailingService mailingService;

	@GetMapping("/send/{email}")
	public ResponseEntity<String> sendCertificate(@PathVariable String email) {
		mailingService.sendTestMail();
		return ResponseEntity.ok("Certificate sent");
	}

	@GetMapping("/validate/{email}")
	public ResponseEntity<Boolean> validateCertificate(@PathVariable String email) {
		return ResponseEntity.ok(certificateService.validateCertificate(email));
	}

}
