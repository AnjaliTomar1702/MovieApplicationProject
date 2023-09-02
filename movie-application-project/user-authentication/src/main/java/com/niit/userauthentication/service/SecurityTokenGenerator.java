package com.niit.userauthentication.service;



import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.dto.MessageDTO;
import com.niit.userauthentication.exception.UserNotEnabledException;

public interface SecurityTokenGenerator {
    MessageDTO generateToken(DatabaseUser databaseUser) throws UserNotEnabledException;
}
