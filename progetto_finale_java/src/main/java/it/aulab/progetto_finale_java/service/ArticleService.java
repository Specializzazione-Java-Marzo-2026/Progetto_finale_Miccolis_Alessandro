package it.aulab.progetto_finale_java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_java.model.Article;
import it.aulab.progetto_finale_java.model.Category;
import it.aulab.progetto_finale_java.model.Image;
import it.aulab.progetto_finale_java.model.User;
import it.aulab.progetto_finale_java.repository.ArticleRepository;

@Service
public class ArticleService {


private final ArticleRepository articleRepository;
private final ImageService imageService;

public ArticleService(ArticleRepository articleRepository, ImageService imageService) {
    this.articleRepository = articleRepository;
    this.imageService = imageService;
}

// -------------------------
// PARTE PUBBLICA
// -------------------------

public List<Article> findAll() {
    return articleRepository.findAllByAcceptedTrueOrderByCreatedAtDesc();
}

public List<Article> findRecentArticles() {
    return articleRepository.findTop5ByAcceptedTrueOrderByCreatedAtDesc();
}

public List<Article> findByCategoryId(Long categoryId) {
    return articleRepository.findAllByCategoryIdAndAcceptedTrueOrderByCreatedAtDesc(categoryId);
}

public List<Article> searchByTerm(String searchTerm) {
    if (searchTerm == null || searchTerm.isBlank()) {
        return articleRepository.findAllByAcceptedTrueOrderByCreatedAtDesc();
    }
    return articleRepository.searchByTitleSubtitleOrCategory(searchTerm.trim());
}

// Vista pubblica autore: solo articoli accettati
public List<Article> findByUserId(Long userId) {
    return articleRepository.findAllByUserIdAndAcceptedTrueOrderByCreatedAtDesc(userId);
}

public Optional<Article> findById(Long id) {
    return articleRepository.findByIdAndAcceptedTrue(id);
}

// -------------------------
// PARTE REVISORE
// -------------------------

public Optional<Article> findByIdForReview(Long id) {
    return articleRepository.findById(id);
}

public List<Article> findPendingReview() {
    return articleRepository.findAllByReviewedFalseOrderByCreatedAtDesc();
}

@Transactional
public Article acceptArticle(Long id) {
    Article article = articleRepository.findById(id).orElseThrow();
    article.setAccepted(true);
    article.setReviewed(true);
    return articleRepository.save(article);
}

@Transactional
public Article rejectArticle(Long id) {
    Article article = articleRepository.findById(id).orElseThrow();
    article.setAccepted(false);
    article.setReviewed(true);
    return articleRepository.save(article);
}

// -------------------------
// CREAZIONE ARTICOLO
// -------------------------

public Article create(String title, String subtitle, String body, User user, Category category) {
    return create(title, subtitle, body, user, category, null);
}

public Article create(String title, String subtitle, String body, User user, Category category, MultipartFile imageFile) {
    Article article = new Article();
    article.setTitle(title);
    article.setSubtitle(subtitle);
    article.setBody(body);
    article.setUser(user);
    article.setCategory(category);
    article.setAccepted(false);
    article.setReviewed(false);

    Article savedArticle = articleRepository.save(article);

    if (imageFile != null && !imageFile.isEmpty()) {
        Image image = imageService.saveFile(imageFile, savedArticle);
        savedArticle.setImage(image);
        articleRepository.save(savedArticle);
    }

    return savedArticle;
}

// -------------------------
// PARTE WRITER - USER STORY 5
// -------------------------

// Dashboard writer: mostra tutti gli articoli del writer, anche non accettati
public List<Article> findAllByWriter(Long userId) {
    return articleRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
}

// Recupera articolo solo se appartiene al writer
public Optional<Article> findByIdAndWriter(Long articleId, Long userId) {
    return articleRepository.findByIdAndUserId(articleId, userId);
}

@Transactional
public Article updateArticle(Long articleId,
                             String title,
                             String subtitle,
                             String body,
                             Category category,
                             User user,
                             MultipartFile imageFile) {

    Article article = articleRepository.findById(articleId).orElseThrow();

    // controllo proprietario
    if (!article.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Non puoi modificare un articolo che non è tuo");
    }

    article.setTitle(title);
    article.setSubtitle(subtitle);
    article.setBody(body);
    article.setCategory(category);

    // EXTRA US5: se modificato, torna in revisione
    article.setAccepted(false);
    article.setReviewed(false);

    Article savedArticle = articleRepository.save(article);

    if (imageFile != null && !imageFile.isEmpty()) {
        Image image = imageService.saveFile(imageFile, savedArticle);
        savedArticle.setImage(image);
        articleRepository.save(savedArticle);
    }

    return savedArticle;
}

@Transactional
public void deleteArticle(Long articleId, User user) {
    Article article = articleRepository.findById(articleId).orElseThrow();

    // controllo proprietario
    if (!article.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Non puoi cancellare un articolo che non è tuo");
    }

    articleRepository.delete(article);
}


}
