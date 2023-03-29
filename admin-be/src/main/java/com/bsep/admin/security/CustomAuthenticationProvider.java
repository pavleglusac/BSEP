package com.bsep.admin.security;

import com.bsep.admin.exception.InvalidLogin;
import com.bsep.admin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;
		String name = customAuthenticationToken.getName();
		String password = customAuthenticationToken.getCredentials().toString();
		String loginToken = customAuthenticationToken.getLoginToken().toString();

		User user = (User) customUserDetailsService.loadUserByUsername(name);
		if (passwordEncoder.matches(password, user.getPassword()) && passwordEncoder.matches(loginToken, user.getLoginToken())) {
			return new CustomAuthenticationToken(user, password, loginToken, user.getAuthorities());
		}
		throw new InvalidLogin("Invalid username or password");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(CustomAuthenticationToken.class);
	}

}
