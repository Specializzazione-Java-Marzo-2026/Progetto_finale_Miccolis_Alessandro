package it.aulab.progetto_finale_java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.progetto_finale_java.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
