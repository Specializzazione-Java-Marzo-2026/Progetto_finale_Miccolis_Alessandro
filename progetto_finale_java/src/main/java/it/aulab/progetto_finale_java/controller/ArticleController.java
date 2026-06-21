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
import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_java.model.Article;
import it.aulab.progetto_finale_java.model.Category;
import it.aulab.progetto_finale_java.model.User;
import it.aulab.progetto_finale_java.service.ArticleService;
import it.aulab.progetto_finale_java.service.CategoryService;
import it.aulab.progetto_finale_java.service.UserService;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, CategoryService categoryService, UserService userService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/article/create")
    public String createForm(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "article/create";
    }

    @PostMapping("/article/store")
    public String storeArticle(@RequestParam String title,
                               @RequestParam String subtitle,
                               @RequestParam String body,
                               @RequestParam Long categoryId,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               Authentication authentication,
                               Model model) {
        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(userEmail).orElseThrow();

        Category category = categoryService.findAll().stream()
                .filter(cat -> cat.getId().equals(categoryId))
                .findFirst()
                .orElseThrow();

        Article article = articleService.create(title, subtitle, body, user, category, imageFile);
        model.addAttribute("article", article);
        model.addAttribute("successMessage", "Articolo salvato con successo.");
        return "article/detail";
    }

    @GetMapping("/article/{id}")
    public String detailArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", article);
        return "article/detail";
    }

    @GetMapping("/revisor/articles")
    public String revisorDashboard(Model model) {
        model.addAttribute("articles", articleService.findPendingReview());
        return "revisor/dashboard";
    }

    @GetMapping("/revisor/articles/{id}")
    public String revisorArticleDetail(@PathVariable Long id, Model model) {
        Article article = articleService.findByIdForReview(id).orElseThrow();
        model.addAttribute("article", article);
        return "revisor/detail";
    }

    @PostMapping("/revisor/articles/{id}/accept")
    public String acceptArticle(@PathVariable Long id) {
        articleService.acceptArticle(id);
        return "redirect:/revisor/articles";
    }

    @PostMapping("/revisor/articles/{id}/reject")
    public String rejectArticle(@PathVariable Long id) {
        articleService.rejectArticle(id);
        return "redirect:/revisor/articles";
    }

    @GetMapping("/category/{categoryId}")
    public String articlesByCategory(@PathVariable Long categoryId, Model model) {
        List<Article> articles = articleService.findByCategoryId(categoryId);
        Category category = categoryService.findAll().stream()
                .filter(cat -> cat.getId().equals(categoryId))
                .findFirst()
                .orElse(null);

        model.addAttribute("articles", articles);
        model.addAttribute("viewTitle", category != null ? "Articoli in: " + category.getName() : "Articoli per categoria");
        return "home";
    }

    @GetMapping("/search")
    public String searchArticles(@RequestParam(name = "searchTerm", required = false) String searchTerm,
                                 Model model) {
        List<Article> articles = articleService.searchByTerm(searchTerm);
        String viewTitle = (searchTerm != null && !searchTerm.isBlank())
                ? "Risultati per: '" + searchTerm.trim() + "'"
                : "Tutti gli articoli";

        model.addAttribute("articles", articles);
        model.addAttribute("viewTitle", viewTitle);
        model.addAttribute("searchTerm", searchTerm);
        return "home";
    }

    @GetMapping("/author/{userId}")
    public String articlesByAuthor(@PathVariable Long userId, Model model) {
        List<Article> articles = articleService.findByUserId(userId);
        User user = userService.findById(userId).orElse(null);

        model.addAttribute("articles", articles);
        model.addAttribute("viewTitle", user != null ? "Articoli di: " + user.getUsername() : "Articoli per autore");
        return "home";
    }

    // -------------------------
    // USER STORY 5 - MODIFICA
    // -------------------------

    @GetMapping("/article/edit/{id}")
    public String editArticleForm(@PathVariable Long id,
                                  Authentication authentication,
                                  Model model) {

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(userEmail).orElseThrow();

        Article article = articleService.findByIdAndWriter(id, user.getId()).orElse(null);

        if (article == null) {
            return "redirect:/writer/dashboard";
        }

        List<Category> categories = categoryService.findAll();
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);

        return "article/edit";
    }

    @PostMapping("/article/update/{id}")
    public String updateArticle(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String subtitle,
                                @RequestParam String body,
                                @RequestParam Long categoryId,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                Authentication authentication) {

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(userEmail).orElseThrow();

        Category category = categoryService.findAll().stream()
                .filter(cat -> cat.getId().equals(categoryId))
                .findFirst()
                .orElseThrow();

        if (articleService.findByIdAndWriter(id, user.getId()).isEmpty()) {
            return "redirect:/writer/dashboard";
        }

        articleService.updateArticle(id, title, subtitle, body, category, user, imageFile);

        return "redirect:/writer/dashboard";
    }

    // -------------------------
    // USER STORY 5 - CANCELLAZIONE
    // -------------------------

    @PostMapping("/article/delete/{id}")
    public String deleteArticle(@PathVariable Long id,
                                Authentication authentication) {

        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userService.findByEmail(userEmail).orElseThrow();

        if (articleService.findByIdAndWriter(id, user.getId()).isEmpty()) {
            return "redirect:/writer/dashboard";
        }

        articleService.deleteArticle(id, user);

        return "redirect:/writer/dashboard";
    }
}