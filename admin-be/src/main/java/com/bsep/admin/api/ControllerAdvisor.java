package com.bsep.admin.api;

import com.bsep.admin.exception.*;
import com.bsep.admin.model.LogType;
import com.bsep.admin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor {

	@Autowired
	private LogService logService;

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseError handleException(Exception e) {
		System.out.println(e.getMessage());
		logService.logAction(LogType.ERROR, "Internal server error.", "Exception caught in ControllerAdvisor. " + e.getMessage());
		return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler(MessageSignatureException.class)
	public ResponseError handleMessageSignatureException(MessageSignatureException e) {
		System.out.println(e.getMessage());
		logService.logAction(LogType.ERROR, "Message signature error.", "Exception caught in ControllerAdvisor. " + e.getMessage());
		return new ResponseError(HttpStatus.PRECONDITION_FAILED, "Message signature error.");
	}

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
		logService.logAction(LogType.WARNING, "User not found.", e.getMessage());
		return new ResponseError(HttpStatus.NOT_FOUND, "User not found.");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CsrNotFoundException.class)
	public ResponseError handleCsrNotFoundException(CsrNotFoundException e) {
		logService.logAction(LogType.WARNING, "CSR not found.", e.getMessage());
		return new ResponseError(HttpStatus.NOT_FOUND, "CSR not found.");
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(InvalidLogin.class)
	public ResponseError handleInvalidLogin(InvalidLogin e) {
		logService.logAction(LogType.WARNING, "Invalid login attempt.", e.getMessage());
		return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CertificateAlreadyRevokedException.class)
	public ResponseError handleCertificateAlreadyRevokedException(CertificateAlreadyRevokedException e) {
		logService.logAction(LogType.WARNING, "Certificate already revoked.", "This user's certificate has already been revoked. " + e.getMessage());
		return new ResponseError(HttpStatus.BAD_REQUEST, "This user's certificate has already been revoked.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRoleException.class)
	public ResponseError handleInvalidRoleException(InvalidRoleException e) {
		logService.logAction(LogType.WARNING, "Invalid role.", e.getMessage());
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
		logService.logAction(LogType.ERROR, msg, e.getMessage());
		return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenRealEstateAction.class)
	public ResponseError handleForbiddenRealEstateAction(ForbiddenRealEstateAction e) {
		logService.logAction(LogType.WARNING, "Forbidden real estate action.", e.getMessage());
		return new ResponseError(HttpStatus.FORBIDDEN, e.getMessage());
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenDeviceAction.class)
	public ResponseError handleForbiddenDeviceAction(ForbiddenDeviceAction e) {
		logService.logAction(LogType.WARNING, "Forbidden device action.", e.getMessage());
		return new ResponseError(HttpStatus.FORBIDDEN, e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RealEstateNotFoundException.class)
	public ResponseError handleRealEstateNotFoundException(RealEstateNotFoundException e) {
		return new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseError handleDeviceNotFoundException(DeviceNotFoundException e) {
		return new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(RuleNotFoundException.class)
	public ResponseError handleRuleNotFoundException(RuleNotFoundException e) {
		return new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRuleException.class)
	public ResponseError handleInvalidRuleException(InvalidRuleException e) {
		return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(MailingException.class)
	public ResponseError handleMailingException(MailingException e) {
		logService.logAction(LogType.ERROR, "Mailing error.", e.getMessage());
		return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DeviceCommunicationExcpetion.class)
	public ResponseError handleDeviceCommunicationException(DeviceCommunicationExcpetion e) {
		logService.logAction(LogType.ERROR, "Device communication error.", e.getMessage());
		return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDeviceException.class)
	public ResponseError handleInvalidDeviceException(InvalidDeviceException e) {
		return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(CsrException.class)
	public ResponseError handleCsrException(CsrException e) {
		logService.logAction(LogType.ERROR, "CSR error.", e.getMessage());
		return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

}