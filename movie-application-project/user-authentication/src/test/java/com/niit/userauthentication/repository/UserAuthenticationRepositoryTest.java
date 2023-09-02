package com.niit.userauthentication.repository;

import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Image;
import com.niit.userauthentication.domain.Role;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(value = SpringExtension.class)
//@DataJpaTest()
//@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
//@ExtendWith(SpringExtension.class)
@DataJpaTest
//@EnableJpaRepositories(basePackageClasses = { DatabaseUserRepository.class, RoleRepository.class })
//@EntityScan(basePackageClasses = { DatabaseUser.class, Role.class, Image.class })
//@SpringBootTest
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserAuthenticationRepositoryTest {

    @Autowired
    DatabaseUserRepository databaseUserRepository;
    @Autowired
    private TestEntityManager entityManager;
    private DatabaseUser databaseUser;

//    @BeforeEach
//    void setUp(){
//        this.databaseUser = new DatabaseUser();
//        this.databaseUser.setId(1L);
//        this.databaseUser.setEmail("testingEmail@test.com");
//        this.databaseUser.setPassword("testing");
//        this.databaseUser.setEnabled(true);
//        this.databaseUser.setImage(new Image(new byte[]{1}));
//        this.databaseUser.setAccountNonLocked(true);
//        this.databaseUser.setCredentialsExpiryDate(LocalDate.ofYearDay(9999, 365));
//        this.databaseUser.setAccountExpiryDate(LocalDate.ofYearDay(9999, 365));
//        this.databaseUser.setRoles(List.of(new Role("ROLE_USER")));
//    }
//
//    @AfterEach
//    void tearDown(){
//        if(this.databaseUser.getId() != null && this.databaseUser.getId() != 0L)
//            this.databaseUserRepository.deleteById(this.databaseUser.getId());
//    }
//
    @Test
    @DisplayName("Test inject components created")
    public void injectedComponentsAreNotNull(){
        assertNotNull(entityManager);
        assertNotNull(this.databaseUserRepository);
    }

    @Test
    @DisplayName("Testing saving user")
    public void testSavingUser(){
        this.databaseUser = this.databaseUserRepository.save(this.databaseUser);
        DatabaseUser databaseUser1 = this.databaseUserRepository.findById(this.databaseUser.getId()).orElse(null);
        assertNotNull(databaseUser1);
        assertEquals(this.databaseUser, databaseUser1);
    }
//
//    @Test
//    @DisplayName("Test getting user by email")
//    public void testGettingUserByEmail(){
//        this.databaseUser = this.databaseUserRepository.save(this.databaseUser);
//        DatabaseUser databaseUser1 = this.databaseUserRepository.findByEmail(this.databaseUser.getEmail());
//        assertNotNull(databaseUser1);
//        assertEquals(this.databaseUser, databaseUser1);
//    }

//    @Test
//    @DisplayName("Testing user not saved when image is not provided")
//    public void testUserNotSavedWhenNoImage(){
//        this.databaseUser.setImage(null);
//        assertThrows(Exception.class, () -> this.databaseUserRepository.save(this.databaseUser));
//    }
//
//    @Test
//    @DisplayName("Testing email id is unique")
//    public void testEmailIdIsUnique(){
//        DatabaseUser databaseUser1 = new DatabaseUser();
//        databaseUser1.setId(2L);
//        databaseUser1.setEmail(this.databaseUser.getEmail());
//        databaseUser1.setPassword(this.databaseUser.getPassword());
//        databaseUser1.setEnabled(this.databaseUser.isEnabled());
//        databaseUser1.setImage(this.databaseUser.getImage());
//        databaseUser1.setAccountNonLocked(this.databaseUser.isAccountNonLocked());
//        databaseUser1.setCredentialsExpiryDate(this.databaseUser.getCredentialsExpiryDate());
//        databaseUser1.setAccountExpiryDate(this.databaseUser.getAccountExpiryDate());
//        databaseUser1.setRoles(this.databaseUser.getRoles());
//        this.databaseUser = this.databaseUserRepository.save(this.databaseUser);
//        databaseUser1 = this.databaseUserRepository.save(databaseUser1);
//        if(databaseUser1 != null)
//            this.databaseUserRepository.deleteById(databaseUser1.getId());
//    }

}
