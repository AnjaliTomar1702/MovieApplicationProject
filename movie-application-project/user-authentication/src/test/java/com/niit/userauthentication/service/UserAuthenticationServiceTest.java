package com.niit.userauthentication.service;


import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Image;
import com.niit.userauthentication.domain.Role;
import com.niit.userauthentication.dto.MessageDTO;
import com.niit.userauthentication.repository.DatabaseUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationServiceTest {

    @Mock
    @Autowired
    private DatabaseUserRepository databaseUserRepository;

    @InjectMocks
    private DatabaseUserService databaseUserService;
    private DatabaseUser databaseUser;

    @BeforeEach
    void setUp(){
        this.databaseUser = new DatabaseUser();
        this.databaseUser.setId(1L);
        this.databaseUser.setEmail("testingEmail@test.com");
        this.databaseUser.setPassword("testing");
        this.databaseUser.setEnabled(true);
        this.databaseUser.setImage(new Image(new byte[]{1}));
        this.databaseUser.setAccountNonLocked(true);
        this.databaseUser.setCredentialsExpiryDate(LocalDate.ofYearDay(9999, 365));
        this.databaseUser.setAccountExpiryDate(LocalDate.ofYearDay(9999, 365));
        this.databaseUser.setRoles(List.of(new Role("ROLE_USER")));
    }

    @AfterEach
    void tearDown(){
        this.databaseUser = null;
    }


//    MessageDTO saveUser(DatabaseUser databaseUser) throws UserEmailAlreadyExistsException;
//    DatabaseUser getUserFromEmailAndPassword(String userDatabase, String password) throws InvalidCredentialsException;
//    Image getImageFromEmail(String email);
    @Test
    @DisplayName("Test Saving User")
    void testSavingUser(){
        when(this.databaseUserRepository.save(any())).thenReturn(this.databaseUser);
        MessageDTO messageDTO = this.databaseUserService.saveUser(this.databaseUser);
        assertEquals(messageDTO, new MessageDTO("User " + this.databaseUser.getEmail() + " registered"));
        verify(this.databaseUserRepository,times(1)).save(any());
    }

    @Test
    @DisplayName("Test Getting User From EmailId And Password")
    void testGettingData(){
        when(this.databaseUserRepository.findByEmail(anyString())).thenReturn(this.databaseUser);
        DatabaseUser databaseUser1 = this.databaseUserService.getUserFromEmailAndPassword(this.databaseUser.getEmail(), this.databaseUser.getPassword());
        assertEquals(this.databaseUser, databaseUser1);
        verify(this.databaseUserRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Test Getting Image From Email")
    void testGettingInStockProducts(){
        when(this.databaseUserRepository.findOptionalByEmail(anyString())).thenReturn(Optional.ofNullable(this.databaseUser));
        Image image1 = this.databaseUserService.getImageFromEmail(this.databaseUser.getEmail());
        assertEquals(image1, databaseUser.getImage());
        verify(databaseUserRepository,times(1)).findOptionalByEmail(anyString());
    }

}
