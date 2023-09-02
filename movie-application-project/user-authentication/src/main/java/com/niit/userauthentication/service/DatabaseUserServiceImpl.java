package com.niit.userauthentication.service;

import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Image;
import com.niit.userauthentication.dto.MessageDTO;
import com.niit.userauthentication.dto.UserSessionDTO;
import com.niit.userauthentication.exception.InvalidCredentialsException;
import com.niit.userauthentication.exception.UserEmailAlreadyExistsException;
import com.niit.userauthentication.exception.UserNotFoundException;
import com.niit.userauthentication.repository.RoleRepository;
import com.niit.userauthentication.repository.DatabaseUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseUserServiceImpl implements DatabaseUserService{

    private final DatabaseUserRepository databaseUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    @PersistenceContext
    private final EntityManager entityManager;
    @Override
    @Transactional
    public MessageDTO saveUser(DatabaseUser databaseUser) throws UserEmailAlreadyExistsException {
        if(this.databaseUserRepository.findByEmail(databaseUser.getEmail()) != null)
            throw new UserEmailAlreadyExistsException();
        DatabaseUser savingDatabaseUser = new DatabaseUser();
        savingDatabaseUser.setEmail(databaseUser.getEmail());
        savingDatabaseUser.setPassword(this.passwordEncoder.encode(databaseUser.getPassword()));
        savingDatabaseUser.setRoles(List.of(this.roleRepository.findByName("ROLE_USER")));
        savingDatabaseUser.setEnabled(true);
        savingDatabaseUser.setImage(databaseUser.getImage());
        return new MessageDTO("User " + this.databaseUserRepository.save(savingDatabaseUser).getEmail() + " registered");
    }

    @Override
    public DatabaseUser getUserFromEmailAndPassword(String email, String password) throws InvalidCredentialsException {
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new InvalidCredentialsException("User does not exist");
        this.entityManager.detach(databaseUser);
        if(this.passwordEncoder.matches(password, databaseUser.getPassword())){
            databaseUser.setPassword(null);
            return databaseUser;
        }
        throw new InvalidCredentialsException("Invalid credentials");
    }

    @Override
    public Image getImageFromEmail(String email) {
        return this.databaseUserRepository.findOptionalByEmail(email)
                .orElseThrow(InvalidCredentialsException::new)
                .getImage();
    }

    @Override
    public Image updateImage(Image image, String email){
        DatabaseUser databaseUser = this.databaseUserRepository.findOptionalByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        databaseUser.getImage().setImage(image.getImage());
        this.databaseUserRepository.save(databaseUser);
        return image;
    }

    @Override
    public void sendEmailPasswordResetMessage(String email, String sessionId) throws Exception, UserNotFoundException {
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new UserNotFoundException();
        this.rabbitTemplate.convertAndSend(this.directExchange.getName(), "user-forgot-password-routing", new UserSessionDTO(email, sessionId));
    }

    @Override
    public boolean resetPassword(String email, String password) throws UserNotFoundException{
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new UserNotFoundException();
        databaseUser.setPassword(this.passwordEncoder.encode(password));
         return this.databaseUserRepository.save(databaseUser) != null;
    }

    @Override
    public void sendEmailVerficationMessage(String email, String sessionId) throws Exception, UserNotFoundException {
        this.rabbitTemplate.convertAndSend(this.directExchange.getName(), "user-verify-email-routing", new UserSessionDTO(email, sessionId));
    }
}
