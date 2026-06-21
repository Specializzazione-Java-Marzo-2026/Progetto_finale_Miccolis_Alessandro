package it.aulab.progetto_finale_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.progetto_finale_java.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
