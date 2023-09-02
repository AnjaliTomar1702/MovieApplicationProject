package com.niit.movieservice.exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(){}

    public InvalidCredentialsException(String message){
        super(message);
    }

}
