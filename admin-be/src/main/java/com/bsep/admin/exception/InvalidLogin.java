package com.bsep.admin.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidLogin extends AuthenticationException {
	public InvalidLogin(String message) {
		super(message);
	}
}
