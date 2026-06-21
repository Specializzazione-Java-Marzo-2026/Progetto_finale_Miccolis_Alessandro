create Table images(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    path VARCHAR(255) NOT NULL,
    article_id BIGINT NOT NULL,
    FOREIGN KEY (article_id) REFERENCES articles(id)
);
