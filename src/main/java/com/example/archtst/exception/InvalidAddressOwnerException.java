package com.example.archtst.exception;

public class InvalidAddressOwnerException extends RuntimeException {
    public InvalidAddressOwnerException(String message) {
        super(message);
    }
}
