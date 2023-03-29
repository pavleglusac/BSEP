package com.bsep.admin.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 600L;
	private final Object principal;
	private Object credentials;

	private Object loginToken;

	public CustomAuthenticationToken(Object principal, Object credentials, Object loginToken) {
		super((Collection)null);
		this.principal = principal;
		this.credentials = credentials;
		this.loginToken = loginToken;
		this.setAuthenticated(false);
	}

	public CustomAuthenticationToken(Object principal, Object credentials, Object loginToken, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.loginToken = loginToken;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public Object getLoginToken() {
		return loginToken;
	}
}
