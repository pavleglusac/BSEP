package com.bsep.admin.pki;

import com.bsep.admin.model.User;
import com.bsep.admin.model.Csr;

import com.bsep.admin.repository.CsrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CsrService {

	@Autowired
	private AdminService adminService;
	@Autowired
	private CsrRepository csrRepository;
	@Autowired
	private KeyService keyService;
	public CsrService() {}

	public List<Csr> findAll() {
		return this.csrRepository.findAll();
	}
	public void processCsr(Csr csr, User user) throws Exception {
		KeyPair keys = keyService.generateKeys();
		keyService.storeKeys(keys, user.getEmail());
		LocalDateTime currentTime = LocalDateTime.now();
		Optional<Csr> existingCsrOpt = this.csrRepository.findByEmail(user.getEmail());
		if (existingCsrOpt.isPresent()) {
			csr = this.updateCsr(existingCsrOpt.get(), csr, currentTime);
		} else {
			csr.setEmail(user.getEmail());
			csr.setCreatedDate(currentTime);
		}
		csrRepository.save(csr);
	}

	private Csr updateCsr(Csr previousCsr, Csr newCsr, LocalDateTime currentTime) {
		previousCsr.setCommonName(newCsr.getCommonName());
		previousCsr.setGivenName(newCsr.getGivenName());
		previousCsr.setSurname(newCsr.getSurname());
		previousCsr.setOrganization(newCsr.getOrganization());
		previousCsr.setOrganizationalUnit(newCsr.getOrganizationalUnit());
		previousCsr.setCountry(newCsr.getCountry());
		previousCsr.setCreatedDate(currentTime);
		return previousCsr;
	}
}