# Atelier 4 - notes sur les services

## A. Injection de dépendances

| Critère | Injection par constructeur | Injection par attribut |
|---|---|---|
| Champ `final` | Oui | Non, en pratique |
| Dépendance visible | Oui, dans la signature | Non, elle est cachée dans la classe |
| Utilisable hors de Spring | Oui, avec un doublon/test fourni au constructeur | Difficile, nécessite de la réflexion ou Spring |
| Signal SonarQube for IDE | Conforme | S6813 peut signaler l'injection par champ |

L'injection par constructeur est retenue : elle rend les dépendances obligatoires, immuables et faciles à tester. Lombok génère ce constructeur avec `@RequiredArgsConstructor`.

## B. Les neuf services

| Interface | Dépendance injectée | Mode | Justification |
|---|---|---|---|
| `IAgenceService` | `IAgenceRepository` | Constructeur | Centralise le CRUD et les contrôles de l'agence. |
| `IEmployeService` | `IEmployeRepository` | Constructeur | Gère les employés sans état mutable. |
| `IVehiculeService` | `IVehiculeRepository` | Constructeur | Contrôle les données métier du véhicule. |
| `IEquipementService` | `IEquipementRepository` | Constructeur | Gère le libellé des équipements. |
| `IClientService` | `IClientRepository` | Constructeur | Centralise les règles d'identification client. |
| `IReservationService` | `IReservationRepository` | Constructeur | Contrôle la cohérence des dates. |
| `IContratService` | `IContratRepository` | Constructeur | Gère le contrat et sa suppression en cascade. |
| `IPaiementService` | `IPaiementRepository` | Constructeur | Lecture seule : le paiement est géré via le contrat. |
| `IMaintenanceService` | `IMaintenanceRepository` | Constructeur | Contrôle les dates et la description. |

Chaque implémentation est un singleton sans état métier mutable ; seul son repository `final` est conservé.

## C. Diagnostic d'erreurs Spring

1. `NoSuchBeanDefinitionException` pour `IContratService` indique généralement une implémentation sans `@Service`, un mauvais package ou un package hors du scan de `com.example.activite`. Ajouter `@Service` à `ContratServiceImpl` et garder la classe sous le package principal (ou configurer explicitement le scan).
2. `NoUniqueBeanDefinitionException` avec `emailNotificateur` et `smsNotificateur` signifie que deux beans satisfont la même interface. Marquer le choix par défaut avec `@Primary`, ou demander un bean précis avec `@Qualifier("emailNotificateur")` au point d'injection.
3. Une dépendance circulaire entre `clientServiceImpl` et `reservationServiceImpl` signifie que chaque constructeur demande l'autre. Extraire la logique partagée vers un troisième service, ou faire dépendre les deux services des repositories. `@Lazy` peut dépanner, mais ne corrige pas la conception du cycle.

## D. SonarQube for IDE

Le plugin SonarQube for IDE n'était pas disponible dans cet environnement, donc aucune analyse n'est revendiquée. À vérifier manuellement dans l'IDE :

| Règle/anomalie | Problème | Correction attendue | Résultat à vérifier |
|---|---|---|---|
| S6813 | Injection par attribut avec `@Autowired` | Champ `final` + constructeur | Plus de signalement après analyse. |
| S112 | `throw new RuntimeException(...)` temporaire | Exception métier `ResourceNotFoundException` | Plus d'exception générique dans le code final. |
| Imports inutiles / code mort | Imports du runner et runner temporaire | Les supprimer | Vérifier les inspections de l'IDE. |

Les démonstrations temporaires de S6813 et S112 ne sont pas laissées dans le projet.

## E. Questions de compréhension

1. Le `new ContratServiceImpl(...)` est réalisé par le conteneur Spring au démarrage, après le scan de `@Service`; le développeur ne l'écrit pas.
2. Un futur contrôleur dépendra de `IContratService` pour respecter l'abstraction : l'implémentation peut changer sans modifier le contrôleur et les tests peuvent fournir un faux service.
3. Un singleton est partagé par toutes les requêtes. S'il conserve par exemple le « client courant » dans un champ, la requête d'un utilisateur peut écraser celle d'un autre.
4. `update` charge l'entité gérée existante pour vérifier son existence, préserver son identifiant et ne modifier que les attributs autorisés.
5. `@Component` déclare une classe automatiquement détectée; `@Bean` déclare, dans une classe de configuration, l'objet retourné par une méthode. `@Primary` choisit le candidat par défaut; `@Qualifier` désigne explicitement un candidat. `IPaiementService` reste sans écriture car, dans ce modèle, le cycle de vie du paiement passe par le contrat.

La règle de coût négatif de l'exemple PDF ne s'applique pas : l'entité `Maintenance` actuelle ne possède pas de champ de coût.
