package com.bsep.admin.exception;

public class ForbiddenDeviceAction extends RuntimeException {
    public ForbiddenDeviceAction(String message) {
        super(message);
    }
}
