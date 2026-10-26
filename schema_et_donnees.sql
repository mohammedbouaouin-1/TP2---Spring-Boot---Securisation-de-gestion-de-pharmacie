CREATE DATABASE IF NOT EXISTS pharma_security_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharma_security_db;

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS medicaments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    prix DECIMAL(10,2) NOT NULL,
    quantite_stock INT NOT NULL,
    code_barre VARCHAR(100)
) ENGINE=InnoDB;

INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ADMIN'),
(2, 'PHARMACIEN'),
(3, 'CAISSIER'),
(4, 'CLIENT');

INSERT INTO medicaments (id, nom, description, prix, quantite_stock, code_barre) VALUES
(1, 'Amoxicilline 500mg', 'Antibiotique a large spectre', 45.00, 50, 'AMX-500'),
(2, 'Doliprane 1000mg', 'Antalgique et antipyretique', 18.50, 120, 'DLP-1000'),
(3, 'Vitamine C 1000mg', 'Complement nutritionnel et fortifiant', 32.00, 40, 'VITC-1000'),
(4, 'Ibuprofene 400mg', 'Anti-inflammatoire et douleurs', 25.00, 60, 'IBU-400'),
(5, 'Betadine Solution 10%', 'Antiseptique local pour plaies', 22.00, 35, 'BET-010')
ON DUPLICATE KEY UPDATE nom=VALUES(nom);