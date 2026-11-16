# Atelier 6 : Affectations, DTO et validation

## Endpoints

Préfixe commun : `http://localhost:8080/api`.

| Opération | Route PUT | Corps JSON | Réponse |
|---|---|---|---|
| Maintenance vers véhicule | `/maintenances/{maintenanceId}/vehicule` | `{"vehiculeId":6}` | `MaintenanceVehiculeResponse` |
| Véhicule vers équipement | `/vehicules/{vehiculeId}/equipements` | `{"equipementId":1}` | `VehiculeEquipementResponse` |

Les identifiants cibles sont dans le corps pour permettre une véritable validation des DTO de requête avec `@Valid`, `@NotNull` et `@Positive`. Les identifiants dans le chemin utilisent `@Positive` et la validation native Spring MVC. Il ne faut pas ajouter `@Validated` à ce controller : Spring MVC gère ici directement les contraintes des paramètres via `HandlerMethodValidationException`. Aucun champ texte artificiel n'est ajouté pour appliquer `@NotBlank`.

`GlobalExceptionHandler` transforme les erreurs de corps et de paramètres en HTTP 400 avec `statut`, `message`, `timestamp`. Les ressources absentes donnent HTTP 404. Les réponses sont des records DTO sans graphe JPA.

## Architecture et persistance

`AffectationController` injecte uniquement `IAffectationService`. `AffectationServiceImpl` injecte les trois repositories et réalise chaque affectation dans une transaction. Le côté propriétaire est modifié : `Maintenance.vehicule` pour le ManyToOne, `Vehicule.equipements` pour le ManyToMany. Les cardinalités, tables et routes CRUD précédentes sont conservées.

L'ajout d'un équipement vérifie les identifiants déjà présents dans la collection. Le véhicule est verrouillé avec une requête native `FOR UPDATE`, compatible MySQL/MariaDB, pour sérialiser deux affectations concurrentes au même véhicule. Le test réel a montré que la syntaxe de verrou générée par le dialecte MySQL Hibernate (`FOR UPDATE OF`) ne convient pas à la version MariaDB de XAMPP ; la requête native corrige ce point sans changer le schéma.

## Vérifications réelles du 16 novembre 2026

- Premier `clean test` : échec, MySQL arrêté (connexion refusée).
- Démarrage du serveur existant `C:/xampp82/mysql` puis `mvnw.cmd clean test` : BUILD SUCCESS, un test de contexte, zéro erreur.
- Nouvelle compilation et tests après correction de la requête de verrou : BUILD SUCCESS.
- Tests et application lancés avec `SPRING_JPA_HIBERNATE_DDL_AUTO=none` / `--spring.jpa.hibernate.ddl-auto=none` pour ne pas modifier le schéma existant.
- Données dédiées créées via les anciens POST : véhicule 6 (`AT6-20261116-01`), maintenance 1 (`Test affectation Atelier 6`), équipement 1 (`GPS test Atelier 6`). Ces données sont conservées pour la démonstration ; aucune donnée préexistante supprimée.
- Affectation maintenance 1 au véhicule 6 : HTTP 200 et DTO contenant les identifiants, les dates, la description et l'immatriculation.
- Affectation véhicule 6 / équipement 1, répétée deux fois : HTTP 200 et DTO contenant les identifiants, l'immatriculation et le libellé.
- Vérification SQL : `maintenance.id_maintenance=1` porte `vehicule_id=6`; le couple `(vehicule_id=6,equipement_id=1)` apparaît exactement une fois dans `vehicule_equipement`.
- Maintenance absente, véhicule absent et équipement absent : quatre cas HTTP 404 vérifiés.
- Corps `{}`, identifiants 0 ou négatifs, et identifiants 0 dans les deux chemins : cinq cas HTTP 400 vérifiés.
- Les neuf collections existantes répondent HTTP 200 ; création d'un véhicule, d'une maintenance et d'un équipement également vérifiée.
- Les deux nouvelles opérations sont présentes dans `/api/v3/api-docs` et dans Swagger UI, groupe « Affectations ».

## Captures à remettre (restent à réaliser)

Aucune capture de réponse Swagger n'est fabriquée. Le navigateur a été ouvert et les deux opérations ont été trouvées dans l'arbre accessible. Les clics ont échoué avec « coordinate input geometry is unavailable », puis « point ... is outside window bounds » ; les tests effectifs ont donc été réalisés par HTTP et SQL.

1. Lancer MySQL et l'application, puis ouvrir <http://localhost:8080/api/swagger-ui.html>.
2. Descendre au groupe **Affectations** et ouvrir `PUT /maintenances/{maintenanceId}/vehicule`.
3. Cliquer **Try it out**, renseigner `maintenanceId=1` et le corps `{"vehiculeId":6}`, puis **Execute**.
4. Capturer l'URL, le code 200 et le JSON de réponse (avec maintenanceId 1 et vehiculeId 6) ; enregistrer dans `screenshots/maintenance-vehicule.png`.
5. Ouvrir `PUT /vehicules/{vehiculeId}/equipements`, renseigner `vehiculeId=6` et `{"equipementId":1}`, puis **Execute**.
6. Capturer le code 200 et le DTO (vehiculeId 6, equipementId 1) ; enregistrer dans `screenshots/vehicule-equipement.png`.

Ces appels peuvent être répétés sans ajouter de doublon à l'association équipement. Adapter les identifiants si les données locales ont changé.
