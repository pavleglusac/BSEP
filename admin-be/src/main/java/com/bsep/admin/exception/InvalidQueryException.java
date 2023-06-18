package com.bsep.admin.exception;

public class InvalidQueryException extends RuntimeException {
    public InvalidQueryException() { }
    public InvalidQueryException(String message) {
        super(message);
    }
}
