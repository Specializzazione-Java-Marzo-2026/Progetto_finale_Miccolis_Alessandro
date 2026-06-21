package it.aulab.progetto_finale_java.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.aulab.progetto_finale_java.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    // PARTE PUBBLICA
    List<Article> findAllByAcceptedTrueOrderByCreatedAtDesc();
    List<Article> findTop5ByAcceptedTrueOrderByCreatedAtDesc();
    List<Article> findAllByCategoryIdAndAcceptedTrueOrderByCreatedAtDesc(Long categoryId);
    List<Article> findAllByUserIdAndAcceptedTrueOrderByCreatedAtDesc(Long userId);
    Optional<Article> findByIdAndAcceptedTrue(Long id);

    // PARTE REVISORE
    List<Article> findAllByReviewedFalseOrderByCreatedAtDesc();

    // DASHBOARD WRITER
    List<Article> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Article> findByIdAndUserId(Long id, Long userId);

    // RICERCA
    @Query("SELECT a FROM Article a WHERE a.accepted = true AND (" +
           "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.subtitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
           ") ORDER BY a.createdAt DESC")
    List<Article> searchByTitleSubtitleOrCategory(@Param("searchTerm") String searchTerm);
}