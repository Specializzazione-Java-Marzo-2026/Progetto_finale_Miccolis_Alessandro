package it.aulab.progetto_finale_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.progetto_finale_java.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByPath(String path);
}
