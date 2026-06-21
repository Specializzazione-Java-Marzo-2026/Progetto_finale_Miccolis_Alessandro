package it.aulab.progetto_finale_java.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.aulab.progetto_finale_java.model.CareerRequest;
import it.aulab.progetto_finale_java.model.User;
import it.aulab.progetto_finale_java.service.CareerRequestService;
import it.aulab.progetto_finale_java.service.UserService;

@Controller
public class OperationController {

    private final CareerRequestService careerRequestService;
    private final UserService userService;

    public OperationController(CareerRequestService careerRequestService, UserService userService) {
        this.careerRequestService = careerRequestService;
        this.userService = userService;
    }

    @GetMapping("/career/request")
    public String careerRequestForm(Model model) {
        model.addAttribute("roleOptions", List.of("ROLE_WRITER", "ROLE_REVISOR"));
        return "career/requestForm";
    }

    @PostMapping("/career/request")
    public String submitCareerRequest(@RequestParam String roleRequested,
                                      @RequestParam String message,
                                      Authentication authentication,
                                      Model model) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(email).orElseThrow();

        try {
            careerRequestService.saveRequest(user, roleRequested, message);
            model.addAttribute("successMessage", "Richiesta inviata con successo. L'admin verrà informato via email.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("roleOptions", List.of("ROLE_WRITER", "ROLE_REVISOR"));
        return "career/requestForm";
    }

    @GetMapping("/admin/career/requests")
    public String adminCareerRequests(Model model) {
        List<CareerRequest> requests = careerRequestService.findPendingRequests();
        model.addAttribute("requests", requests);
        return "admin/careerRequests";
    }

    @GetMapping("/admin/career/requests/{id}")
    public String adminCareerRequestDetail(@PathVariable Long id, Model model) {
        CareerRequest request = careerRequestService.findById(id);
        model.addAttribute("request", request);
        return "admin/careerRequestDetail";
    }

    @PostMapping("/admin/career/requests/{id}/accept")
    public String acceptCareerRequest(@PathVariable Long id) {
        careerRequestService.processRequest(id);
        return "redirect:/admin/career/requests";
    }
}
