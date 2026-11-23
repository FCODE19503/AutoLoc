# Atelier 7 — Recherches Spring Data JPA par KeyWords

## Repositories et relations traversées

Les trois premières méthodes sont des requêtes dérivées KeyWords : Spring Data analyse leur nom au démarrage et construit la requête SQL. La quatrième, véhicules par ID maintenance, utilise désormais une requête JPQL explicite avec `@Query`. Aucun filtrage en mémoire ni changement de cardinalité n'est nécessaire.

| Repository | Signature | Chemin JPA |
|---|---|---|
| `IVehiculeRepository` | `List<Vehicule> findDistinctByReservations_Contrat_IdContrat(Long contratId)` | Vehicule.reservations → Reservation.contrat → Contrat.idContrat |
| `IContratRepository` | `List<Contrat> findByReservation_Vehicule_IdVehicule(Long vehiculeId)` | Contrat.reservation → Reservation.vehicule → Vehicule.idVehicule |
| `IMaintenanceRepository` | `List<Maintenance> findByVehicule_IdVehicule(Long vehiculeId)` | Maintenance.vehicule → Vehicule.idVehicule |
| `IVehiculeRepository` | `List<Vehicule> findVehiculesByMaintenanceJPQL(@Param("maintenanceId") Long maintenanceId)` | JPQL : Vehicule.maintenances → Maintenance.idMaintenance |

`findBy` exprime la recherche, `_` sépare explicitement les propriétés traversées et `Distinct` évite des véhicules répétés lors des jointures. La requête native de verrou de l'Atelier 6 est conservée séparément et n'intervient pas dans ces recherches.

La quatrième méthode utilise les entités et propriétés Java dans la requête JPQL suivante, et non les noms des tables SQL :

```java
@Query("""
    SELECT DISTINCT v
    FROM Vehicule v
    JOIN v.maintenances m
    WHERE m.idMaintenance = :maintenanceId
    """)
List<Vehicule> findVehiculesByMaintenanceJPQL(@Param("maintenanceId") Long maintenanceId);
```

`VehiculeServiceImpl.findVehiculesByMaintenance` appelle cette méthode tout en conservant le contrôle d'existence, les DTO, les validations et la route `GET /api/maintenances/{maintenanceId}/vehicules`.

Vérification de cette adaptation JPQL le 23 novembre 2026 : `mvnw.cmd clean test` réussit (un test, zéro erreur), puis Spring Boot démarre. `GET /api/maintenances/1/vehicules` retourne HTTP 200 avec le véhicule 6 ; les identifiants `0`, `-1` et `abc` donnent 400, `999999` donne 404 et la maintenance 2 sans véhicule donne `200 []`. Vérifications en lecture seule avec les modifications de schéma désactivées ; aucune donnée existante modifiée.

## Services, DTO et validation

`RechercheController` utilise uniquement les interfaces des services existants :

- `IVehiculeService` / `VehiculeServiceImpl` : `findVehiculesByContrat`, `findVehiculesByMaintenance`.
- `IContratService` / `ContratServiceImpl` : `findContratsByVehicule`.
- `IMaintenanceService` / `MaintenanceServiceImpl` : `findMaintenancesByVehicule`.

Les implémentations injectent les repositories nécessaires via un constructeur Lombok et des champs `final`. Aucun service n'injecte un autre service. Chaque recherche utilise une transaction en lecture seule et vérifie d'abord `existsById` sur la ressource source. Une source absente lève `ResourceNotFoundException` (404), tandis qu'une source présente sans association donne une liste vide (200).

Les DTO `VehiculeRechercheResponse`, `ContratRechercheResponse` et `MaintenanceRechercheResponse` contiennent uniquement les identifiants et les données principales : immatriculation/marque/modèle/tarif/statut, date/montant/validité du contrat, dates/description de maintenance. Aucun graphe JPA récursif n'est exposé. Les streams servent uniquement à convertir les résultats SQL en DTO, sans filtrer les entités.

Les paramètres URL sont obligatoires par construction de la route et validés par `@Positive`. Spring MVC et le `GlobalExceptionHandler` existant traitent les violations en 400. Un identifiant non numérique produit aussi 400. Un chemin sans identifiant ne correspond pas à cette route.

## Endpoints et démonstration

Préfixe : `http://localhost:8080/api`.

| Route GET | Exemple testé | Résultat principal |
|---|---|---|
| `/contrats/{contratId}/vehicules` | `/contrats/1/vehicules` | véhicule 5 |
| `/vehicules/{vehiculeId}/contrats` | `/vehicules/5/contrats` | contrat 1, réservation 2 |
| `/vehicules/{vehiculeId}/maintenances` | `/vehicules/6/maintenances` | maintenance 1 |
| `/maintenances/{maintenanceId}/vehicules` | `/maintenances/1/vehicules` | véhicule 6 |

Exemples de réponses (champs représentatifs) :

```json
[{"idVehicule":5,"immatriculation":"789TUN123","marque":"Peugeot","modele":"208","categorie":"CITADINE","tarifJournalier":120.00,"statut":"DISPONIBLE"}]
```

```json
[{"idContrat":1,"dateSignature":"2026-10-12","montantTotal":360.00,"valide":true,"idReservation":2}]
```

```json
[{"idMaintenance":1,"dateDebut":"2026-11-16","dateFin":"2026-11-17","description":"Test affectation Atelier 6","idVehicule":6}]
```

Les deux recherches de véhicules peuvent contenir zéro ou un élément : un contrat référence une seule réservation, laquelle référence au plus un véhicule; une maintenance référence au plus un véhicule. Le retour sous forme de liste respecte la consigne sans transformer les associations JPA.

## Résultats réels du 22 novembre 2026

- Compilation Maven réussie. Premier test de contexte échoué car MySQL était arrêté ; connexion refusée confirmée avec le client MySQL.
- Démarrage de l'installation XAMPP existante, puis `mvnw.cmd clean test` : **BUILD SUCCESS**, un test exécuté, zéro échec et zéro erreur.
- Vérifications effectuées avec `SPRING_JPA_HIBERNATE_DDL_AUTO=none`, sans modification du schéma.
- Spring Boot démarre sur 8080, contexte `/api`. Les quatre méthodes dérivées sont reconnues par Spring Data et les quatre routes sont présentes dans `/api/v3/api-docs`.
- Les quatre recherches positives du tableau ci-dessus ont répondu HTTP 200, avec des tableaux JSON de DTO.
- SQL confirme `contrat(1).reservation_id=2`, `reservation(2).vehicule_id=5` et `maintenance(1).vehicule_id=6`.
- Quatre recherches avec source 999999 : HTTP 404.
- Identifiants 0/négatifs sur chacune des quatre routes : HTTP 400 ; identifiant `abc` sur la route contrat : HTTP 400.
- Véhicule 1 sans contrat et sans maintenance : HTTP 200 avec `[]` pour les deux recherches.
- Pour couvrir aussi les deux autres cas vides, création minimale via les POST existants d'un contrat 2 sans réservation et d'une maintenance 2 sans véhicule : HTTP 200 avec `[]` pour leurs recherches. Ces deux données de démonstration sont conservées. Aucune donnée préexistante n'a été supprimée.
- Les neuf GET de collections des ateliers précédents répondent HTTP 200.
- Les PUT de l'Atelier 6 (`maintenance 1 → véhicule 6`, `véhicule 6 → équipement 1`) répondent HTTP 200 avec leurs DTO. Ils répètent les associations existantes.

## Captures Swagger à remettre

Les captures ne sont pas réalisées automatiquement pour cet atelier. Aucune image de résultat n'est fabriquée. Pour produire les quatre captures :

1. Démarrer MySQL puis l'application (`mvnw.cmd spring-boot:run`).
2. Ouvrir <http://localhost:8080/api/swagger-ui.html>, groupe **Recherches KeyWords**.
3. Ouvrir successivement les quatre GET du tableau de démonstration, cliquer **Try it out**, saisir respectivement contratId `1`, vehiculeId `5`, vehiculeId `6`, maintenanceId `1`, puis **Execute**.
4. Capturer pour chaque opération la route, l'identifiant, le code 200 et le tableau JSON de réponse.
5. Enregistrer les images dans `screenshots/` : `vehicules-par-contrat.png`, `contrats-par-vehicule.png`, `maintenances-par-vehicule.png`, `vehicules-par-maintenance.png`.

Les données locales peuvent évoluer : vérifier les identifiants via les collections avant une nouvelle démonstration.
