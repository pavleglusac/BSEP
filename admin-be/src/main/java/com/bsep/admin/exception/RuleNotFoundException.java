package com.bsep.admin.exception;

public class RuleNotFoundException extends RuntimeException {
    public RuleNotFoundException (String message) {
        super(message);
    }
}
