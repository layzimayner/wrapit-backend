package com.wrap.it.exception;

public class TooLargeAmountException extends RuntimeException {
    public TooLargeAmountException(String message) {
        super(message);
    }
}
