package it.aulab.progetto_finale_java.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_java.model.Article;
import it.aulab.progetto_finale_java.model.Image;
import it.aulab.progetto_finale_java.repository.ImageRepository;
import jakarta.transaction.Transactional;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Value("${supabase.image}")
    private String supabaseImage;

    private final RestTemplate restTemplate = new RestTemplate();

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void saveImageOnDB(String url, Article article) {
        url = url.replace(supabaseBucket, supabaseImage);
        imageRepository.save(Image.builder().path(url).article(article).build());
    }

    @Override
    public String saveImageOnCloud(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String nameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String extension = getFileExtension(nameFile);

        String uploadUrl = supabaseUrl + supabaseBucket + nameFile;

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "image/" + extension);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
        restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        return supabaseUrl + supabaseImage + nameFile;
    }

    @Override
    public Image saveFile(MultipartFile file, Article article) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try {
            String publicUrl = saveImageOnCloud(file);
            return imageRepository.save(Image.builder().path(publicUrl).article(article).build());
        } catch (Exception e) {
            throw new RuntimeException("Impossibile salvare l'immagine su Supabase", e);
        }
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Async
    @Transactional
    @Override
    public void deleteImageFromCloud(String imagePath) throws IOException {
        String url = imagePath.replace(supabaseImage, supabaseBucket);

        imageRepository.deleteByPath(imagePath);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        System.out.println(response.getBody());
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "jpeg";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }
}
