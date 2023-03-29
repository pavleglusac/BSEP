package com.bsep.admin.pki.service;

import com.bsep.admin.model.User;
import com.bsep.admin.pki.dto.CertificateDto;
import com.bsep.admin.pki.dto.CsrDto;
import com.bsep.admin.pki.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CsrService {

	@Autowired
	private AdminService adminService;

	public CsrService() {

	}

	public void processCsr(CsrDto csr, User user) {
		// create private and public key
		// create csr and save to db
		// store to folder (create folder if it doesn't exist
		System.out.println(csr);
		System.out.println(user);
	}

}