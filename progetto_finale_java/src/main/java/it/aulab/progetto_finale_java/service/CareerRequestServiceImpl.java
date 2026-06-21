package it.aulab.progetto_finale_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.progetto_finale_java.model.CareerRequest;
import it.aulab.progetto_finale_java.model.Role;
import it.aulab.progetto_finale_java.model.User;
import it.aulab.progetto_finale_java.repository.CareerRequestRepository;
import it.aulab.progetto_finale_java.repository.RoleRepository;
import it.aulab.progetto_finale_java.repository.UserRepository;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    private final CareerRequestRepository careerRequestRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final String adminEmail;

    public CareerRequestServiceImpl(CareerRequestRepository careerRequestRepository,
                                    RoleRepository roleRepository,
                                    UserRepository userRepository,
                                    EmailService emailService,
                                    @Value("${app.admin.email:admin@example.com}") String adminEmail) {
        this.careerRequestRepository = careerRequestRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.adminEmail = adminEmail;
    }

    @Override
    @Transactional
    public CareerRequest saveRequest(User user, String roleRequested, String message) {
        boolean alreadyRequested = careerRequestRepository
                .existsByUserAndRoleRequestedAndProcessedFalse(user, roleRequested);

        if (alreadyRequested) {
            throw new IllegalArgumentException("Hai già inviato una richiesta per questo ruolo.");
        }

        boolean hasRole = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleRequested));

        if (hasRole) {
            throw new IllegalArgumentException("Hai già il ruolo richiesto.");
        }

        CareerRequest request = new CareerRequest();
        request.setUser(user);
        request.setRoleRequested(roleRequested);
        request.setMessage(message);
        CareerRequest saved = careerRequestRepository.save(request);

        String subject = "Nuova richiesta di collaborazione";
        String text = String.format("L'utente %s (%s) ha richiesto il ruolo %s.\n\nMessaggio:\n%s",
                user.getUsername(), user.getEmail(), roleRequested, message);
        emailService.sendSimpleMessage(adminEmail, subject, text);

        return saved;
    }

    @Override
    public List<CareerRequest> findPendingRequests() {
        return careerRequestRepository.findAllByProcessedFalseOrderByCreatedAtDesc();
    }

    @Override
    public CareerRequest findById(Long id) {
        return careerRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata"));
    }

    @Override
    @Transactional
    public CareerRequest processRequest(Long id) {
        CareerRequest request = findById(id);
        if (request.isProcessed()) {
            throw new IllegalStateException("Questa richiesta è già stata processata.");
        }

        String roleName = request.getRoleRequested();
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        User user = request.getUser();
        user.addRole(role);
        userRepository.save(user);

        request.setProcessed(true);
        return careerRequestRepository.save(request);
    }
}
