package com.bsep.admin.auth;

import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import com.bsep.admin.security.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider tokenProvider;

	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginRequest.getEmail(),
				loginRequest.getPassword()
		));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accessToken = tokenProvider.createAccessToken(authentication);
		Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
		return new TokenResponse(accessToken, expiresAt);
	}
}
