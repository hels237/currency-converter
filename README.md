
# API de Conversion de Devises

Cette application est une API REST développée avec Spring Boot, permettant de convertir une somme d'argent d'une devise à une autre en utilisant des taux de change récupérés dynamiquement depuis une API externe (Exchangerate-API.com).

---

## Objectif

Créer une API qui convertit une somme d'argent d'une devise à une autre en utilisant des taux de change récupérés dynamiquement.

---

## Fonctionnalités

* Endpoint de conversion prenant en entrée la devise source, la devise cible et le montant.
* Appel à une API externe pour obtenir les taux de change (Exchangerate-API.com).
* Gestion des erreurs (devise invalide, problème de connexion à l'API externe, montant invalide).

---

## Technologies utilisées

* Java 17+
* Spring Boot
* Spring WebClient (pour appeler l'API externe)
* Spring Validation
* Lombok
* Swagger (OpenAPI 3)

---

## Lancer le projet

### 1. Cloner le dépôt

```bash
git clone [https://github.com/ngaland/currency_converter.git]
cd currency_converter
```
### 2. Démarrer l'application
  * mvn spring-boot:run
  * L'application démarrera sur http://localhost:8080

### Points d'entrée de l'API (Endpoints)
  * URL de base de l'API: http://localhost:8080/api/v1/convert
  * Documentation Swagger UI: http://localhost:8080/swagger-ui.html

### cle d'API converter dans propertie

### Endpoint de Conversion de Devises
   * Méthode HTTP: GET
   * Chemin: /api/v1/convert
   * Paramètres de requête:
     - source (String, obligatoire): Code ISO de la devise source (ex: USD, XAF)
     - target (String, obligatoire): Code ISO de la devise cible (ex: GBP, JPY)
     - amount (double, obligatoire): Montant à convertir (doit être supérieur à zéro)
     - 
### Exemple de requête via URL: http://localhost:8080/swagger-ui/index.html#/Conversion%20de%20Devises/convertCurrency
  * Response body:
  * {
    "sourceAmount": 4000,
    "sourceCurrency": "USD",
    "convertedAmount": 2313455.5999999996,
    "targetCurrency": "XAF",
    "exchangeRate": 578.3639,
    "conversionTime": "2025-05-28T16:14:26.445168843"
    }
