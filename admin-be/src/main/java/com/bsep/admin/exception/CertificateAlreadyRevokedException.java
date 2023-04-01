package com.bsep.admin.exception;

public class CertificateAlreadyRevokedException extends RuntimeException {
    public CertificateAlreadyRevokedException() {
    }

    public CertificateAlreadyRevokedException(String message) {
        super(message);
    }
}
