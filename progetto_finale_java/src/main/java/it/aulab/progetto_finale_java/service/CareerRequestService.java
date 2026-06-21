package it.aulab.progetto_finale_java.service;

import java.util.List;

import it.aulab.progetto_finale_java.model.CareerRequest;
import it.aulab.progetto_finale_java.model.User;

public interface CareerRequestService {
    CareerRequest saveRequest(User user, String roleRequested, String message);
    List<CareerRequest> findPendingRequests();
    CareerRequest findById(Long id);
    CareerRequest processRequest(Long id);
}
