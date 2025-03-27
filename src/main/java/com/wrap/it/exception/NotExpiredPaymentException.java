package com.wrap.it.exception;

public class NotExpiredPaymentException extends RuntimeException {
    public NotExpiredPaymentException(String message) {
        super(message);
    }
}
