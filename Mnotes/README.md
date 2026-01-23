[🇫🇷 Read in French](README.fr.md) | [🇬🇧 Read in English](README.md)

# mnotes - Patient Notes Management Microservice

---
**Project**: MicroDiab  
**Organization**: MediLabo Solutions  
**Author**: michaudj  
**Date**: January 2026  
**Version**: 1.0.0
---

## 📌 Role
This microservice manages **patient notes** (CR – Create, Read) for the **MicroDiab** application.
It is developed with **Spring Boot 3.5.8**, **Java 21 (Eclipse Adoptium JDK 21.0.2.13)**,
and uses **MongoDB 6.0** as its database. It is secured via **Spring Security**
and integrated into the **MicroDiab** ecosystem (Eureka, Zipkin, Feign).

**Key Features**:
- Create and read notes.
- Data validation with **Jakarta Validation**.
- Interactive documentation via **Swagger UI** and **Javadoc**.
- Distributed tracing with **Zipkin** and **Micrometer Tracing**.
- Containerized with **Docker** (compatible with `docker-compose`).

---

## 📦 Dependencies
**Internal Microservices**

| Microservice      | Role                              | Port |
|-------------------|-----------------------------------|------|
| **eureka-server** | Service discovery                 | 9102 |
| **zipkin**        | Distributed tracing               | 9411 |
| **mgateway**      | API Gateway + Authentication      | 9010 |

**Databases**

| Database   | Type  | Port  | Docker Volume         |
|------------|-------|-------|-----------------------|
| MongoDB    | NoSQL | 27017 | `mongodb-mnotes-data` |

In the `docker-compose.yml` file, the MongoDB database for the mnotes microservice is configured as an independent service.
- **Image used**: `mongo:6.0`
- **Container name**: `mongodb-mnotes`
- **Database name**: `prod_notes`
- **Data volume**: `mongodb-mnotes-data`

**Communication**: Feign (calls from `clientui` and `mrisk`)
**Tests**: JaCoCo report available in `target/`
**Network**: microdiab-network (shared with other microservices).

**Security**: The gateway applies centralized authentication,
but each microservice remains independently secured to prevent accidental exposure.
- **External Security**: Spring Security (centralized authentication via `mgateway`)
- **Internal Security**: Spring Security (Basic Auth for inter-microservice communications)

---

## 📊 Monitoring and Documentation
| Tool       | URL                                                                  | Description        |
|------------|----------------------------------------------------------------------|--------------------|
| Actuator   | [http://localhost:9002/actuator](http://localhost:9002/actuator)     | Metrics and health |
| Swagger UI | [http://localhost:9002/swagger-ui](http://localhost:9002/swagger-ui) | API Documentation  |
| Javadoc    | [http://localhost:9002/apidocs](http://localhost:9002/apidocs)       | Java Documentation |

---

## 🛠 Prerequisites
- Java 21
- Spring Boot 3.5.8
- Spring Cloud 2025.0.0
- Maven
- MongoDB 6.0
- Docker
- Environment variables:
  - `MONGO_HOST=mongodb-mnotes`
  - `MONGO_PORT=27017`
  - `EUREKA_SERVER_HOST=eureka-server`
  - `EUREKA_SERVER_PORT=9102`

---

## 🚀 Main Endpoints
| Method | Endpoint         | Description                          |
|--------|------------------|--------------------------------------|
| GET    | `/notes/{patId}` | Lists all notes for a patId          |
| POST   | `/notes`         | Creates a new note                   |

---
