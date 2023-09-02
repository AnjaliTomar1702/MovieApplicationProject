package com.niit.userauthentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Image;
import com.niit.userauthentication.domain.Role;
import com.niit.userauthentication.dto.MessageDTO;
import com.niit.userauthentication.dto.UserDTO;
import com.niit.userauthentication.service.AdminService;
import com.niit.userauthentication.service.DatabaseUserService;
import com.niit.userauthentication.service.SecurityTokenGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
public class UserAuthenticationControllerTest {

    @Mock
    private DatabaseUserService databaseUserService;

    @Mock
    private SecurityTokenGenerator securityTokenGenerator;
    @Mock
    private AdminService adminService;

    @InjectMocks
    private UserAuthenticationController userAuthenticationController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
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
        mockMvc = MockMvcBuilders.standaloneSetup(userAuthenticationController).build();
    }

    @AfterEach
    void tearDown(){
        databaseUser = null;
    }

//    /api/v1/login
//    /api/v1/admin/login
//    /api/v1/save
//    /api/v1/image
//    /api/v1/admin/users
//    /api/v1/admin/users
//    /api/v1/admin/roles

    @Test
    @DisplayName("Test login")
    void testGettingData() throws Exception{
        when(this.securityTokenGenerator.generateToken(any(DatabaseUser.class))).thenReturn(new MessageDTO("Token 123"));
        when(this.databaseUserService.getUserFromEmailAndPassword(this.databaseUser.getEmail(), this.databaseUser.getPassword())).thenReturn(this.databaseUser);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(this.databaseUser.getEmail());
        userDTO.setPassword(this.databaseUser.getPassword());
        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentType()));
        verify(this.databaseUserService, times(1)).getUserFromEmailAndPassword(this.databaseUser.getEmail(), this.databaseUser.getPassword());
    }


    private String jsonToString(Object o){
        String result;
        try{
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(o);
        }
        catch (JsonProcessingException jsonMappingException){
            result="Invalid object";
        }
        return result;
    }

}
