package com.wrap.it.exception;

public class StripeSessionException extends RuntimeException {
    public StripeSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
