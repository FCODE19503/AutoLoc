# AutoLoc

Projet pédagogique de gestion de location de véhicules, développé dans le projet Maven `activite` au fil des Ateliers 1 à 5. Package racine : `com.example.activite`.

## Technologies

- Java 17, Maven et Maven Wrapper
- Spring Boot 4.1.1, Spring MVC et Spring Data JPA / Hibernate
- MySQL (environnement local XAMPP), Lombok
- SpringDoc 3.1.1, OpenAPI et Swagger UI

## Fonctionnalités des ateliers

Le projet rassemble la mise en place de Spring Boot, la modélisation JPA et ses associations, les repositories, l'injection de dépendances et les services métier, puis l'API REST documentée.

Neuf entités sont disponibles : Agence, Employe, Vehicule, Equipement, Client, Reservation, Contrat, Paiement et Maintenance. Les services valident les champs obligatoires, les montants et la cohérence des dates. Les controllers utilisent les interfaces de service et un gestionnaire central traduit les erreurs métier en réponses HTTP 400 ou 404.

Les routes `/api/agences`, `/api/employes`, `/api/vehicules`, `/api/equipements`, `/api/clients`, `/api/reservations`, `/api/contrats` et `/api/maintenances` proposent le CRUD : GET, GET par identifiant, POST (201), PUT (200), DELETE (204). `/api/paiements` propose uniquement GET et GET par identifiant ; POST retourne 405. La relation un-à-un Contrat–Paiement conserve une cascade depuis le contrat.

## Lancement local

1. Installer un JDK 17 et démarrer le serveur de base de données local.
2. Configurer les variables d'environnement si nécessaire : `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`. Par défaut, le projet utilise `jdbc:mysql://localhost:3306/activite?createDatabaseIfNotExist=true`, l'utilisateur local `root` et un mot de passe vide. Ne jamais committer de véritables identifiants.
3. Dans le dossier du projet, exécuter sous Windows :

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Sous Linux/macOS : `./mvnw clean test` puis `./mvnw spring-boot:run`.

Le test existant charge le contexte Spring et nécessite une base accessible. La configuration utilise `spring.jpa.hibernate.ddl-auto=update` : Hibernate peut adapter le schéma au démarrage. Pour vérifier une base existante sans modifier son schéma, définir `SPRING_JPA_HIBERNATE_DDL_AUTO=none` pendant les tests et le lancement.

L'API utilise le port 8080 et le préfixe `/api` :

- Swagger UI : <http://localhost:8080/api/swagger-ui.html>
- OpenAPI JSON : <http://localhost:8080/api/v3/api-docs>

## Documentation

- [Notes des services et de l'injection de dépendances](docs/service-notes.md)
- [Notes de l'API REST](docs/api-notes.md)
- [Collection Postman à importer](docs/postman/AutoLoc-Atelier5.postman_collection.json), variable `baseUrl` : `http://localhost:8080`

Adapter les identifiants et compléter les corps JSON des requêtes Postman avant les opérations d'écriture. Ces opérations modifient les données de la base configurée.

`target/`, les configurations locales IntelliJ/VS Code, les fichiers `.env` et les clés privées sont exclus de Git. Le dépôt ne contient aucune sauvegarde de la base de données.
