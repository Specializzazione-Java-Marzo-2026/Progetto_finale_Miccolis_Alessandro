package it.aulab.progetto_finale_java.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.aulab.progetto_finale_java.model.Article;
import it.aulab.progetto_finale_java.model.User;
import it.aulab.progetto_finale_java.service.ArticleService;
import it.aulab.progetto_finale_java.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final ArticleService articleService;

    public UserController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/writer/dashboard")
    public String writerDashboard(Authentication authentication, Model model) {

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

        User user = userService.findByEmail(userEmail).orElseThrow();

        List<Article> articles = articleService.findAllByWriter(user.getId());

        model.addAttribute("articles", articles);
        model.addAttribute("writer", user);

        return "writer/dashboard";
    }
}