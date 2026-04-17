package com.example.archtst.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super("User with " + s + " not found");
    }
}
