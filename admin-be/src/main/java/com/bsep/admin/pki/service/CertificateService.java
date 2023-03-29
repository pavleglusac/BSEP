package com.bsep.admin.pki.service;

import com.bsep.admin.pki.dto.CertificateDto;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

	public void processCertificate(CertificateDto cert) {
		// create certificate
		// save to db
		// store to folder (create folder if it doesn't exist)
		System.out.println(cert);
	}

}
