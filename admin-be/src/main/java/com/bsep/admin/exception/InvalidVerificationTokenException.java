package com.bsep.admin.exception;

public class InvalidVerificationTokenException extends RuntimeException {
	public InvalidVerificationTokenException(String message) {
		super(message);
	}
}
