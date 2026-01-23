[🇫🇷 Lire en français](README.fr.md) | [🇬🇧 Read in English](README.md)

# mpatient - Microservice de gestion des Patients

---
**Projet** : MicroDiab  
**Organisation** : MediLabo Solutions  
**Auteur** : michaudj  
**Date** : Janvier 2026  
**Version** : 1.0.0
---

## 📌 Rôle
Ce microservice gère les **données des patients** (CRU – Create, Read, Update) pour l'application **MicroDiab**. 
Il est développé avec **Spring Boot 3.5.8**, **Java 21 (Eclipse Adoptium JDK 21.0.2.13)**, 
et utilise **MySQL 8.0** comme base de données. Il est sécurisé via **Spring Security** 
et intégré à l'écosystème **MicroDiab** (Eureka, Zipkin, Feign).

**Fonctionnalités clés** :
- Création, lecture, mise à jour de patients.
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

| Base de données | Type | Port | Volume Docker         |
|-----------------|------|------|-----------------------|
| MySQL           | SQL  | 3306 | `mysql-mpatient-data` |

Dans le fichier `docker-compose.yml`, la base de données MySQL pour le microservice mpatient est configurée comme un service indépendant.
- **Image utilisée** : `mysql:8.0`
- **Nom du conteneur** : `mysql-mpatient`
- **Nom de la base de données** : `patientservice`
- **Volume pour les données** : `mysql-mpatient-data`

**Communication** : Feign (appels depuis `clientui` et `mrisk`)  
**Tests** : Rapport JaCoCo disponible dans target/  
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
| Actuator   | [http://localhost:9001/actuator](http://localhost:9001/actuator)     | Métriques et santé |
| Swagger UI | [http://localhost:9001/swagger-ui](http://localhost:9001/swagger-ui) | Documentation API  |
| Javadoc    | [http://localhost:9001/apidocs](http://localhost:9001/apidocs)       | Documentation Java |

---

## 🛠 Prérequis
- Java 21
- Spring Boot 3.5.8
- Spring Cloud 2025.0.0
- Maven
- MySQL (version 8+)
- Docker
- Variables d'environnement :
    - `MYSQL_HOST=mysql-mpatient`
    - `MYSQL_PORT=3306`
    - `MYSQL_DATABASE=patientservice`
    - `EUREKA_SERVER_HOST=eureka-server`
    - `EUREKA_SERVER_PORT=9102`

---

## 🚀 Endpoints principaux
| Méthode | Endpoint                | Description                     |
|---------|-------------------------|---------------------------------|
| GET     | `/patients`             | Liste tous les patients         |
| GET     | `/patients/{id}`        | Récupère un patient par ID      |
| POST    | `/patients`             | Crée un nouveau patient         |
| PUT     | `/patients/{id}`        | Met à jour un patient           |

---


