package com.niit.pushnotification.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(){}

    public InvalidCredentialsException(String message){
        super(message);
    }

}
