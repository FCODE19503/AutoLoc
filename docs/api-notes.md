# Atelier 5 - API REST et SpringDoc

## A. Endpoints

| Controller | Méthodes et routes | Service | Succès | Erreurs |
|---|---|---|---|---|
| AgenceController | GET/POST `/agences`, GET/PUT/DELETE `/agences/{id}` | `IAgenceService` | 200, 201, 204 | 400, 404 |
| EmployeController | GET/POST `/employes`, GET/PUT/DELETE `/employes/{id}` | `IEmployeService` | 200, 201, 204 | 400, 404 |
| VehiculeController | GET/POST `/vehicules`, GET/PUT/DELETE `/vehicules/{id}` | `IVehiculeService` | 200, 201, 204 | 400, 404 |
| EquipementController | GET/POST `/equipements`, GET/PUT/DELETE `/equipements/{id}` | `IEquipementService` | 200, 201, 204 | 400, 404 |
| ClientController | GET/POST `/clients`, GET/PUT/DELETE `/clients/{id}` | `IClientService` | 200, 201, 204 | 400, 404 |
| ReservationController | GET/POST `/reservations`, GET/PUT/DELETE `/reservations/{id}` | `IReservationService` | 200, 201, 204 | 400, 404 |
| ContratController | GET/POST `/contrats`, GET/PUT/DELETE `/contrats/{id}` | `IContratService` | 200, 201, 204 | 400, 404 |
| PaiementController | GET `/paiements`, GET `/paiements/{id}` | `IPaiementService` | 200 | 404, 405 pour POST |
| MaintenanceController | GET/POST `/maintenances`, GET/PUT/DELETE `/maintenances/{id}` | `IMaintenanceService` | 200, 201, 204 | 400, 404 |

Le préfixe commun est `/api`, défini par `server.servlet.context-path`, et n'est donc jamais répété dans les mappings Java.

## B. Annotations Jackson

`@JsonIgnore` masque les collections inverses inutiles (`Agence.employes`, `Client.reservations`, collections de `Vehicule`, etc.). Cela évite de repartir vers le parent pendant la sérialisation tout en conservant les relations utilisables dans les corps POST, par exemple `Reservation.client` et `Reservation.vehicule`.

`@JsonManagedReference("contrat-paiement")` sur `Contrat.paiement` et `@JsonBackReference("contrat-paiement")` sur `Paiement.contrat` représentent le sens sérialisé de la relation un-à-un. Le paiement apparaît dans un contrat; le contrat n'est pas répété dans ce paiement. Les deux annotations sont appropriées lorsque les deux côtés forment une relation parent-enfant.

## C. Postman et Swagger

Postman sert à envoyer et rejouer des requêtes HTTP, avec des corps JSON et des scénarios d'erreurs. La collection fournie utilise `{{baseUrl}}`, par défaut `http://localhost:8080`.

OpenAPI est le contrat machine-readable décrivant les routes, types et réponses. SpringDoc examine les controllers Spring et génère ce contrat à `/api/v3/api-docs`; Swagger UI l'affiche et permet de l'essayer à `/api/swagger-ui.html`. Exemple : dans Swagger UI, ouvrir `POST /api/reservations`, fournir les identifiants client/véhicule, puis vérifier le `201` ou le `400` si la fin précède le début.

## D. SonarQube for IDE

SonarQube for IDE n'était pas disponible dans cet environnement, donc aucune analyse n'est déclarée comme exécutée. Deux contrôles pertinents à lancer dans l'IDE :

| Règle / problème | Correction appliquée / attendue |
|---|---|
| S6813, injection par attribut | Les controllers et services utilisent un champ `final` et `@RequiredArgsConstructor`; aucun `@Autowired` de champ. |
| Exceptions HTTP incohérentes | `GlobalExceptionHandler` convertit `ResourceNotFoundException` en 404 et `IllegalArgumentException` en 400 avec une réponse JSON stable. |

## E. Questions de compréhension

1. `DispatcherServlet` reçoit les requêtes HTTP. Il consulte les mappings construits à partir de `@RequestMapping`, `@GetMapping`, etc., puis appelle la méthode du controller correspondante.
2. `ContratController` dépend de `IContratService` pour rester découplé de l'implémentation et des détails JPA; on peut changer l'implémentation ou la simuler en test sans modifier le controller.
3. POST renvoie 201 car une ressource est créée; DELETE renvoie 204 car il n'y a plus de contenu à renvoyer. Une ressource absente est un 404, pas un 500, car c'est une situation fonctionnelle attendue.
4. `PaiementController` est en lecture seule : le cycle de vie du paiement relève du contrat. `POST /api/paiements` n'a aucun mapping et retourne donc 405 Method Not Allowed.
5. Exposer directement des entités JPA peut déclencher des boucles JSON, charger des relations paresseuses ou révéler des données inutiles. `@JsonManagedReference`/`@JsonBackReference` et `@JsonIgnore` limitent précisément ce qui est sérialisé. À terme, des DTO peuvent offrir un contrat HTTP encore plus strict.
