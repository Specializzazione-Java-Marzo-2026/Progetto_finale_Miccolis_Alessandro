package it.aulab.progetto_finale_java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.progetto_finale_java.model.Category;
import it.aulab.progetto_finale_java.service.CategoryService;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String adminCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @GetMapping("/admin/categories/create")
    public String createCategoryForm() {
        return "admin/categoryForm";
    }

    @PostMapping("/admin/categories/create")
    public String createCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        categoryService.create(name);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria creata con successo.");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/categories/{id}/edit")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id).orElseThrow();
        model.addAttribute("category", category);
        return "admin/categoryForm";
    }

    @PostMapping("/admin/categories/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                                 @RequestParam String name,
                                 RedirectAttributes redirectAttributes) {
        categoryService.update(id, name);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiornata con successo.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Categoria eliminata con successo.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}
