package com.wrap.it.exception;

public class TakenNameException extends RuntimeException {
    public TakenNameException(String message) {
        super(message);
    }
}
