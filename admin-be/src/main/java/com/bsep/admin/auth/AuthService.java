package com.bsep.admin.auth;

import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import com.bsep.admin.security.CustomAuthenticationToken;
import com.bsep.admin.security.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider tokenProvider;


	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(new CustomAuthenticationToken(
				loginRequest.getEmail(),
				loginRequest.getPassword(),
				loginRequest.getLoginToken()
		));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accessToken = tokenProvider.createAccessToken(authentication);
		Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
		return new TokenResponse(accessToken, expiresAt);
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
		System.out.println(generatedString);
		return generatedString;
	}
}
