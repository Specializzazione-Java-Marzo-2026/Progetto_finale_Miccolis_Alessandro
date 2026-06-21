package it.aulab.progetto_finale_java.dto;

import java.time.LocalDateTime;

import it.aulab.progetto_finale_java.model.Category;
import it.aulab.progetto_finale_java.model.Image;
import it.aulab.progetto_finale_java.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ArticleDTO {
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDateTime publishDate;
    private User user;
    private Category category;
    private Image image;

}