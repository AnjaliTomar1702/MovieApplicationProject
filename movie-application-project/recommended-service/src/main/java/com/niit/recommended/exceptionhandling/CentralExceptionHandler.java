package com.niit.recommended.exceptionhandling;

import com.niit.recommended.dto.ErrorMessageDTO;
import com.niit.recommended.exception.*;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class CentralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= InvalidCredentialsException.class)
    public ResponseEntity<ErrorMessageDTO> handleUnauthorizedException(InvalidCredentialsException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value= IOException.class)
    public ResponseEntity<ErrorMessageDTO> handleIOException(IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value= TokenExpiredException.class)
    public ResponseEntity<ErrorMessageDTO> handleTokenExpiredException(TokenExpiredException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value= MalformedJwtException.class)
    public ResponseEntity<ErrorMessageDTO> handleMalformedJwtException(MalformedJwtException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= UserNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleUserNotFoundException(UserNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value= RoleNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleRoleNotFoundException(RoleNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value= NullPointerException.class)
    public ResponseEntity<ErrorMessageDTO> handleNullPointerException(NullPointerException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value= UserEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageDTO> handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value= GenreNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleGenreNotFoundException(GenreNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value= ContentNotFoundException.class)
    public ResponseEntity<ErrorMessageDTO> handleContentNotFoundException(ContentNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessageDTO> handleException(Exception e) throws Exception {
        if(e instanceof InvalidCredentialsException
                || e instanceof IOException
                || e instanceof TokenExpiredException
                || e instanceof MalformedJwtException
                || e instanceof UserNotFoundException
                || e instanceof RoleNotFoundException
                || e instanceof NullPointerException
                || e instanceof UserEmailAlreadyExistsException
                || e instanceof GenreNotFoundException
                || e instanceof ContentNotFoundException)
            throw e;
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
