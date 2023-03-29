package com.bsep.admin.pki;

import com.bsep.admin.data.IssuerData;
import com.bsep.admin.data.SubjectData;
import com.bsep.admin.model.User;
import com.bsep.admin.pki.dto.CsrDto;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cert.path.CertPath;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

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