package com.niit.userauthentication.service;

import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Role;
import com.niit.userauthentication.exception.RoleNotFoundException;
import com.niit.userauthentication.exception.UserNotFoundException;
import com.niit.userauthentication.repository.DatabaseUserRepository;
import com.niit.userauthentication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final DatabaseUserRepository databaseUserRepository;
    private final RoleRepository roleRepository;
    @Override
    public List<DatabaseUser> getUsers() {
        return this.databaseUserRepository.findAll();
    }

    @Override
    public List<DatabaseUser> getUsersContainingString(String email) {
        return this.databaseUserRepository.findByEmailContaining(email);
    }

    @Override
    public boolean blockUser(String email) throws UserNotFoundException {
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new UserNotFoundException();
        databaseUser.setEnabled(false);
        return !this.databaseUserRepository.save(databaseUser).isEnabled();
    }

    @Override
    public boolean unblockUser(String email) {
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new UserNotFoundException();
        databaseUser.setEnabled(true);
        return this.databaseUserRepository.save(databaseUser).isEnabled();
    }

    @Override
    public DatabaseUser addRole(String email, String roleName) throws UserNotFoundException, RoleNotFoundException {
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new UserNotFoundException();
        Role role = this.roleRepository.findByName(roleName);
        if(role == null)
            throw new RoleNotFoundException();
        if(databaseUser.getRoles().stream().anyMatch(i -> i.toString().equals(role.toString())))
            return databaseUser;
        databaseUser.getRoles().add(role);
        return this.databaseUserRepository.save(databaseUser);
    }

    @Override
    public DatabaseUser removeRole(String email, String roleName) throws UserNotFoundException, RoleNotFoundException{
        DatabaseUser databaseUser = this.databaseUserRepository.findByEmail(email);
        if(databaseUser == null)
            throw new UserNotFoundException();
        Role role = this.roleRepository.findByName(roleName);
        if(role == null)
            throw new RoleNotFoundException();
        databaseUser.setRoles(databaseUser.getRoles().stream().filter(i -> !i.toString().equals(role.toString())).toList());
        return this.databaseUserRepository.saveAndFlush(databaseUser);
    }

    @Override
    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public DatabaseUser updateUser(DatabaseUser databaseUser) throws UserNotFoundException{
        DatabaseUser savedDatabaseUser = this.databaseUserRepository.findById(databaseUser.getId())
                .orElseThrow(UserNotFoundException::new);
        savedDatabaseUser.setRoles(databaseUser.getRoles());
        savedDatabaseUser.setEnabled(databaseUser.isEnabled());
        return this.databaseUserRepository.save(savedDatabaseUser);
    }
}
