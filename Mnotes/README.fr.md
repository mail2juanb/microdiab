[🇫🇷 Lire en français](README.fr.md) | [🇬🇧 Read in English](README.md)

# mnotes - Microservice de gestion des Notes

---
**Projet** : MicroDiab  
**Organisation** : MediLabo Solutions  
**Auteur** : michaudj  
**Date** : Janvier 2026  
**Version** : 1.0.0
---

## 📌 Rôle
Ce microservice gère les **notes des patients** (CR – Create, Read) pour l'application **MicroDiab**.
Il est développé avec **Spring Boot 3.5.8**, **Java 21 (Eclipse Adoptium JDK 21.0.2.13)**,
et utilise **MongoDB 6.0** comme base de données. Il est sécurisé via **Spring Security**
et intégré à l'écosystème **MicroDiab** (Eureka, Zipkin, Feign).

**Fonctionnalités clés** :
- Création, lecture de notes.
- Validation des données avec **Jakarta Validation**.
- Documentation interactive via **Swagger UI** et **Javadoc**.
- Traçage distribué avec **Zipkin** et **Micrometer Tracing**.
- Containerisé avec **Docker** (compatible avec `docker-compose`).

---

## 📦 Dépendances
**Microservices Internes**

| Microservice      | Rôle                              | Port |
|-------------------|-----------------------------------|------|
| **eureka-server** | Découverte de services            | 9102 |
| **zipkin**        | Traçage distribué                 | 9411 |
| **mgateway**      | Passerelle API + Authentification | 9010 |

**Bases de Données**

| Base de données | Type  | Port  | Volume Docker         |
|-----------------|-------|-------|-----------------------|
| MongoDB         | NoSQL | 27017 | `mongodb-mnotes-data` |

Dans le fichier `docker-compose.yml`, la base de données MongoDB pour le microservice mnotes est configurée comme un service indépendant.
- **Image utilisée** : `mongo:6.0`
- **Nom du conteneur** : `mongodb-mnotes`
- **Nom de la base de données** : `prod_notes`
- **Volume pour les données** : `mongodb-mnotes-data`

**Communication** : Feign (appels depuis `clientui` et `mrisk`)  
**Tests** : Rapport JaCoCo disponible dans `target/`  
**Réseau** : microdiab-network (partagé avec les autres microservices).  

**Sécurité** : La gateway applique une authentification centralisée, 
mais chaque microservice reste sécurisé indépendamment afin d’éviter 
toute exposition accidentelle.  
* **Sécurité externe** : Spring Security (authentification centralisée via `mgateway`)  
* **Sécurité interne** : Spring Security (Basic Auth pour les communications inter-microservices)

---

## 📊 Monitoring et Documentation
| Outil      | URL                                                                  | Description        |
|------------|----------------------------------------------------------------------|--------------------|
| Actuator   | [http://localhost:9002/actuator](http://localhost:9002/actuator)     | Métriques et santé |
| Swagger UI | [http://localhost:9002/swagger-ui](http://localhost:9002/swagger-ui) | Documentation API  |
| Javadoc    | [http://localhost:9002/apidocs](http://localhost:9002/apidocs)       | Documentation Java |

---

## 🛠 Prérequis
- Java 21
- Spring Boot 3.5.8
- Spring Cloud 2025.0.0
- Maven
- MongoDB 6.0
- Docker
- Variables d'environnement :
  - `MONGO_HOST=mongodb-mnotes`
  - `MONGO_PORT=27017`
  - `EUREKA_SERVER_HOST=eureka-server`
  - `EUREKA_SERVER_PORT=9102`

---

## 🚀 Endpoints principaux
| Méthode | Endpoint         | Description                          |
|---------|------------------|--------------------------------------|
| GET     | `/notes/{patId}` | Liste toutes les notes pour un patId |
| POST    | `/notes`         | Crée une nouvelle note               |

---