package com.niit.userauthentication.service;

import com.niit.userauthentication.domain.DatabaseUser;
import com.niit.userauthentication.domain.Role;
import com.niit.userauthentication.exception.RoleNotFoundException;
import com.niit.userauthentication.exception.UserNotFoundException;

import java.util.List;

public interface AdminService {
    List<DatabaseUser> getUsers();
    List<DatabaseUser> getUsersContainingString(String email);
    boolean blockUser(String email) throws UserNotFoundException;
    boolean unblockUser(String email) throws UserNotFoundException;
    DatabaseUser addRole(String email, String roleName) throws UserNotFoundException, RoleNotFoundException;
    DatabaseUser removeRole(String email, String roleName) throws UserNotFoundException, RoleNotFoundException;
    List<Role> getRoles();
    DatabaseUser updateUser(DatabaseUser databaseUser) throws UserNotFoundException;
}
