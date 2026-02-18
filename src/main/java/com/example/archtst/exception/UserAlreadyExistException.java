package com.example.archtst.exception;

public class UserAlreadyExistException extends RuntimeException
{
    public UserAlreadyExistException(String s)
    {
        super("User with id " + s + " aleady exists");
    }
}