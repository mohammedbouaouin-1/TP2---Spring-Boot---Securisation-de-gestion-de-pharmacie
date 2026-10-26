# TP 2 : Securisation de gestion de pharmacie

Module : Spring Boot - MicroServices  
Professeur : Youness IDRISSI KHAMLICHI  
Filiere : Ingenierie Informatique - ENSA Fes  

---

## 1. Objectif du TP

Mettre en oeuvre la securite d'une application web (authentification et controle d'acces base sur les roles) en utilisant Spring Boot, Spring Security et MySQL.

### Matrice des roles et acces

| Role | Acces autorise |
|---|---|
| **ADMIN** | Gerer les utilisateurs, les medicaments, les stocks (Acces complet) |
| **PHARMACIEN** | Consulter et modifier les stocks, vendre des medicaments |
| **CAISSIER** | Enregistrer les ventes uniquement |
| **CLIENT** | Consulter les medicaments disponibles (Lecture seule) |

---

## 2. Structure du projet

```
tp2 springboot/
|-- pom.xml
|-- mvn.cmd / mvnw.cmd
|-- schema_et_donnees.sql
|-- README.md
\-- src/
    \-- main/
        |-- java/ma/ensaf/pharma_security/
        |   |-- config/
        |   |   \-- SecurityConfig.java
        |   |-- controller/
        |   |   |-- AdminController.java
        |   |   |-- LoginController.java
        |   |   |-- MedicamentController.java
        |   |   \-- VenteController.java
        |   |-- model/
        |   |   |-- Medicament.java
        |   |   |-- Role.java
        |   |   \-- User.java
        |   |-- repository/
        |   |   |-- MedicamentRepository.java
        |   |   |-- RoleRepository.java
        |   |   \-- UserRepository.java
        |   |-- service/
        |   |   |-- CustomUserDetailsService.java
        |   |   \-- InitData.java
        |   \-- PharmaSecurityApplication.java
        \-- resources/
            |-- application.properties
            \-- templates/
                |-- access-denied.html
                |-- admin.html
                |-- home.html
                |-- login.html
                |-- medicaments.html
                |-- medicament-form.html
                \-- ventes.html
```

---

## 3. Scenarios de test (Comptes pre-configures)

A l'initialisation, `InitData` peuple automatiquement la base de donnees avec les comptes de test et les mots de passe chiffres avec BCrypt :

| Utilisateur | Mot de passe | Role | Droits |
|---|---|---|---|
| `admin` | `admin123` | ADMIN | Tout (Administration, Medicaments, Ventes) |
| `pharma` | `pharma123` | PHARMACIEN | Medicaments, modification de stock, Ventes |
| `caisse` | `caisse123` | CAISSIER | Ventes uniquement |
| `client` | `client123` | CLIENT | Consultation medicaments (Lecture seule) |

---

## 4. Configuration MySQL (`application.properties`)

```properties
spring.application.name=pharma-security
server.port=8086

spring.datasource.url=jdbc:mysql://localhost:3306/pharma_security_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=true
```

---

## 5. Lancement de l'application

Dans le dossier `tp2 springboot` :

```powershell
.\mvn.cmd spring-boot:run
```

L'application est ensuite accessible sur :
```
http://localhost:8086/login
```

---

## 6. Verification des regles de securite

1. **Tentative d'acces sans connexion** : Toute URL protegee (ex: `/home`, `/admin`, `/medicaments`) redirige automatiquement vers `/login`.
2. **Connexion en tant que `client`** :
   - Acces a `/medicaments` : Les boutons "Nouveau medicament", "Modifier", "Supprimer" ne sont pas visibles (lecture seule).
   - Tentative d'acces manuel a `/admin` : Redirection vers `/access-denied` (Erreur 403).
3. **Connexion en tant que `caisse`** :
   - Acces a `/ventes` pour enregistrer des ventes.
   - Tentative d'acces a `/admin` ou `/medicaments/nouveau` : Erreur 403.
4. **Connexion en tant que `pharma`** :
   - Acces aux medicaments avec modification de stock et ventes.
   - Tentative d'acces a `/admin` : Erreur 403.
5. **Connexion en tant que `admin`** :
   - Acces a tous les modules, y compris la gestion des utilisateurs (`/admin`).