package com.niit.userauthentication.repository;

import com.niit.userauthentication.domain.DatabaseUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseUserRepository extends JpaRepository<DatabaseUser, Long> {

    DatabaseUser findByEmail(String email);
    @EntityGraph(attributePaths = "image")
    Optional<DatabaseUser> findOptionalByEmail(String email);

    List<DatabaseUser> findByEmailContaining(String email);
}
