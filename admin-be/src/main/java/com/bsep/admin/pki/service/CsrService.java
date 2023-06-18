package com.bsep.admin.pki.service;

import com.bsep.admin.exception.CsrException;
import com.bsep.admin.exception.CsrNotFoundException;
import com.bsep.admin.model.CsrStatus;
import com.bsep.admin.model.User;

import com.bsep.admin.model.Csr;

import com.bsep.admin.pki.dto.CsrDto;
import com.bsep.admin.repository.CsrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CsrService {

	@Autowired
	private CsrRepository csrRepository;

	@Autowired
	private KeyService keyService;

	public List<Csr> findAll() {
		return this.csrRepository.findAll();
	}

	public List<CsrDto> findAllCsr() {
		List<Csr> csrs = findAll();
		List<CsrDto> dtos = new ArrayList<>();
		for (Csr csr : csrs) {
			if (csr.getStatus() != CsrStatus.PENDING) continue;
			try{
				String key = this.findPublicKeyForUser(csr.getEmail());
				dtos.add(new CsrDto(csr, key));
			} catch (Exception e){continue;}
		}
		return dtos;
	}
	public String findPublicKeyForUser(String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return this.keyService.findPublicKeyForUser(email).toString();
	}
	public void processCsr(Csr csr, User user) {
		try {
			KeyPair keys = keyService.generateKeys();
			keyService.storeKeys(keys, user.getEmail());
			LocalDateTime currentTime = LocalDateTime.now();
			Optional<Csr> existingCsrOpt = this.csrRepository.findByEmailAndStatus(user.getEmail(), CsrStatus.PENDING);
			if (existingCsrOpt.isPresent()) {
				csr = this.updateCsr(existingCsrOpt.get(), csr, currentTime);
			} else {
				csr.setEmail(user.getEmail());
				csr.setCreationDate(currentTime);
			}
			csrRepository.save(csr);
		} catch (Exception e) {
			throw new CsrException("Failed to create CSR");
		}
	}

	private Csr updateCsr(Csr previousCsr, Csr newCsr, LocalDateTime currentTime) {
		previousCsr.setCommonName(newCsr.getCommonName());
		previousCsr.setGivenName(newCsr.getGivenName());
		previousCsr.setSurname(newCsr.getSurname());
		previousCsr.setOrganization(newCsr.getOrganization());
		previousCsr.setOrganizationalUnit(newCsr.getOrganizationalUnit());
		previousCsr.setCountry(newCsr.getCountry());
		previousCsr.setCreationDate(currentTime);
		return previousCsr;
	}

	public Csr getCsrByUser(String email) {
		Optional<Csr> csrOpt = this.csrRepository.findByEmailAndStatus(email, CsrStatus.PENDING);
		return csrOpt.orElseThrow(() -> new CsrNotFoundException("Csr not found"));
	}

	public void saveCsr(Csr csr) {
		this.csrRepository.save(csr);
	}

	public String denyCsr(UUID id) {
		Optional<Csr> csrOpt = this.csrRepository.findById(id);
		Csr csr = csrOpt.orElseThrow(() -> new CsrNotFoundException("Csr not found. Deny failed."));
		csr.setStatus(CsrStatus.REJECTED);
		this.csrRepository.save(csr);
		return "Deny Certificate signing request successful.";
	}
}