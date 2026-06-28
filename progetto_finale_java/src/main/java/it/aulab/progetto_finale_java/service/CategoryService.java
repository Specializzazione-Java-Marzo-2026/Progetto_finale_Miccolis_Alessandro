package it.aulab.progetto_finale_java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.aulab.progetto_finale_java.model.Category;
import it.aulab.progetto_finale_java.repository.ArticleRepository;
import it.aulab.progetto_finale_java.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public CategoryService(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category create(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Category update(Long id, String name) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        if (articleRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Non puoi eliminare una categoria associata a uno o più articoli.");
        }
        categoryRepository.deleteById(id);
    }
}
