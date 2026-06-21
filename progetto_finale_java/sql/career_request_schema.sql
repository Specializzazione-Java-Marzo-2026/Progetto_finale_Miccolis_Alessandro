-- MySQL schema for career_request
DROP TABLE IF EXISTS career_request;

CREATE TABLE career_request (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(500) NOT NULL,
    is_checked TINYINT(1) DEFAULT 0,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_career_request_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_career_request_role FOREIGN KEY (role_id) REFERENCES roles(id)
);