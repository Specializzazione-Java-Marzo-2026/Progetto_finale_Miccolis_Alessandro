package it.aulab.progetto_finale_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.progetto_finale_java.model.CareerRequest;
import it.aulab.progetto_finale_java.model.User;

public interface CareerRequestRepository extends JpaRepository<CareerRequest, Long> {
    List<CareerRequest> findAllByProcessedFalseOrderByCreatedAtDesc();
    boolean existsByUserAndRoleRequestedAndProcessedFalse(User user, String roleRequested);
}
