package it.aulab.progetto_finale_java.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.aulab.progetto_finale_java.dto.RegisterDTO;
import it.aulab.progetto_finale_java.model.Article;
import it.aulab.progetto_finale_java.service.ArticleService;
import it.aulab.progetto_finale_java.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final ArticleService articleService;

    public AuthController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Article> articles = articleService.findRecentArticles();
        model.addAttribute("articles", articles);
        model.addAttribute("viewTitle", "Ultimi articoli");
        return "home";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(registerDTO);
            model.addAttribute("successMessage", "Registrazione completata. Puoi effettuare il login.");
            model.addAttribute("registerDTO", new RegisterDTO());
            return "register";
        } catch (IllegalArgumentException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
