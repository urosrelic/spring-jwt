package com.urosrelic.spring_jwt.exception;

public class WrongCredentialsException extends Exception{
    public WrongCredentialsException(String message) {
        super(message);
    }
}
