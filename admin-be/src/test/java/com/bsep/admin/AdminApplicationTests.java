package com.bsep.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@SpringBootTest
class AdminApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
//		System.out.println(generateRandomToken(15));
//		System.out.println(generateRandomToken(15));
//		System.out.println(generateRandomToken(15));
//		System.out.println(generateRandomToken(15));

		System.out.println("-----------------");
		System.out.println(passwordEncoder.encode("Rgx>m--in=Yc%UZ"));
	}

	public String generateRandomToken(int length) {
		int leftLimit = 35;
		int rightLimit = 126;
		SecureRandom random = new SecureRandom();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;
	}

}
