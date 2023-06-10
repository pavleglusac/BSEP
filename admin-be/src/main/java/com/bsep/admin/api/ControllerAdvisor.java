package com.bsep.admin.api;

import com.bsep.admin.exception.*;
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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CertificateAlreadyRevokedException.class)
	public ResponseError handleCertificateAlreadyRevokedException(CertificateAlreadyRevokedException e) {
		return new ResponseError(HttpStatus.BAD_REQUEST, "This user's certificate has already been revoked.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRoleException.class)
	public ResponseError handleInvalidRoleException(InvalidRoleException e) {
		return new ResponseError(HttpStatus.BAD_REQUEST, "Given role is invalid.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidQueryException.class)
	public ResponseError handleInvalidQueryException(InvalidQueryException e) {
		return new ResponseError(HttpStatus.BAD_REQUEST, "Given query is invalid.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	public ResponseError handleRuntimeException(RuntimeException e) {
		String msg = "Runtime error!";
		if (e.getMessage() != null && !e.getMessage().isBlank()) {
			msg = e.getMessage();
		}
		return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenRealEstateAction.class)
	public ResponseError handleForbiddenRealEstateAction(ForbiddenRealEstateAction e) {
		return new ResponseError(HttpStatus.FORBIDDEN, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenDeviceAction.class)
	public ResponseError handleForbiddenDeviceAction(ForbiddenDeviceAction e) {
		return new ResponseError(HttpStatus.FORBIDDEN, e.getMessage());
	}

}