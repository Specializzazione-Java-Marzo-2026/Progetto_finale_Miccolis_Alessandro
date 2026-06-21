package it.aulab.progetto_finale_java.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_java.model.Article;
import it.aulab.progetto_finale_java.model.Image;

public interface ImageService {

    void saveImageOnDB(String url, Article article);
    String saveImageOnCloud(MultipartFile file) throws Exception;
    void deleteImageFromCloud(String imagePath) throws IOException;
    Image saveFile(MultipartFile file, Article article);
    Optional<Image> findById(Long id);
}
