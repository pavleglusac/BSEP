package com.bsep.admin.auth;

import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import com.bsep.admin.model.User;
import com.bsep.admin.repository.UserRepository;
import com.bsep.admin.security.CustomAuthenticationToken;
import com.bsep.admin.security.TokenProvider;
import jakarta.servlet.http.Cookie;
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

	@Autowired
	private UserRepository userRepository;


	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
		String secret = generateRandomToken(20);

		Authentication authentication = authenticationManager.authenticate(new CustomAuthenticationToken(
				loginRequest.getEmail(),
				loginRequest.getPassword(),
				loginRequest.getLoginToken(),
				secret
		));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accessToken = tokenProvider.createAccessToken(authentication);
		Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
		// add cookie with secret
		Cookie cookie = new Cookie("kuki", secret);
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);
		((User)authentication.getPrincipal()).setLoginAttempts(0);
		userRepository.save((User)authentication.getPrincipal());
		return new TokenResponse(accessToken, expiresAt);
	}

	public String generateRandomToken(int length) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'()*+-./:<=>?@[]^_`{|}~";
		SecureRandom random = new SecureRandom();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(allowedChars.length());
			buffer.append(allowedChars.charAt(randomIndex));
		}
		String generatedString = buffer.toString();
		System.out.println(generatedString);
		return generatedString;
	}
}
