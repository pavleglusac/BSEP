package com.bsep.admin.api;

import com.bsep.admin.exception.CsrNotFoundException;
import com.bsep.admin.exception.InvalidLogin;
import com.bsep.admin.exception.ResponseError;
import com.bsep.admin.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseError handleValidationException(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();

		e.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage())
		);

		e.getBindingResult().getGlobalErrors().forEach(error ->
				errors.put(error.getObjectName(), error.getDefaultMessage())
		);

		return new ResponseError(HttpStatus.BAD_REQUEST, "Field validation failed.", errors);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseError handleUserNotFoundException(UserNotFoundException e) {
		return new ResponseError(HttpStatus.NOT_FOUND, "User not found.");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CsrNotFoundException.class)
	public ResponseError handleCsrNotFoundException(CsrNotFoundException e) {
		return new ResponseError(HttpStatus.NOT_FOUND, "CSR not found.");
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(InvalidLogin.class)
	public ResponseError handleInvalidLogin(InvalidLogin e) {
		return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
	}

}