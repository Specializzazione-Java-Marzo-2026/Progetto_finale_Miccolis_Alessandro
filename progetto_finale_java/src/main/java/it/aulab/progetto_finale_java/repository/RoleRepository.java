package it.aulab.progetto_finale_java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.progetto_finale_java.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
