package com.niit.userauthentication.service;

import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Image;
import com.niit.userauthentication.dto.MessageDTO;
import com.niit.userauthentication.exception.InvalidCredentialsException;
import com.niit.userauthentication.exception.UserEmailAlreadyExistsException;
import com.niit.userauthentication.exception.UserNotFoundException;

public interface DatabaseUserService{
    MessageDTO saveUser(DatabaseUser databaseUser) throws UserEmailAlreadyExistsException;

    DatabaseUser getUserFromEmailAndPassword(String userDatabase, String password) throws InvalidCredentialsException;

    Image getImageFromEmail(String email);

    Image updateImage(Image image, String email);

    void sendEmailPasswordResetMessage(String email, String sessionId) throws Exception, UserNotFoundException;

    void sendEmailVerficationMessage(String email, String sessionId) throws Exception, UserNotFoundException;
    boolean resetPassword(String email, String password) throws UserNotFoundException;
}
