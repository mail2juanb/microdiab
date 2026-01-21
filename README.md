
[🇫🇷 Read in French](README.fr.md) | [🇬🇧 Read in English](README.md)

# MicroDiab – Microservices Overview

---
**Project**: MicroDiab  
**Organization**: MediLabo Solutions  
**Author**: michaudj  
**Date**: January 2026  
**Version**: 1.0.0  
---

## 📌 Overview
The **MicroDiab** application is a microservices solution for diabetes analysis and management.
It consists of several microservices, each with a specific role, and uses **Spring Boot 3.5.8**,
**Java 21**, **Spring Cloud 2025.0.0**, **Docker**, and MySQL and MongoDB databases.

### 🌐 Global Architecture
- **Service Discovery**: Eureka Server
- **Distributed Tracing**: `zipkin`
- **Security**: Centralized authentication via `mgateway`
- **Communication**: Feign Client, Spring Cloud Gateway
- **Containerization**: Docker (shared network: `microdiab-network`)

---

## 📦 List of Microservices

### Microservice `mgateway`
- **Role**: Central API Gateway, dynamic routing, centralized authentication, distributed tracing.
- **Port**: 9010
- **Database**: MySQL R2DBC (userservice)
- **Features**: Dynamic routing via Eureka, Spring Security, security headers propagation, Swagger UI documentation.
- **Dependencies**: `eureka-server`, `zipkin`, `mpatient`, `mnotes`, `mrisk`, `clientui`
- **Useful URLs**:
    - Actuator: [http://localhost:9010/actuator](http://localhost:9010/actuator)
    - Zipkin: [http://localhost:9411/zipkin](http://localhost:9411/zipkin)
    - Javadoc: [http://localhost:9010/apidocs](http://localhost:9010/apidocs)

### Microservice `clientui`
- **Role**: Front-end microservice, user interface display via Thymeleaf.
- **Port**: 8090
- **Database**: None
- **Features**: Display patients, medical notes, diabetes risk level, communication via Feign.
- **Dependencies**: `eureka-server`, `zipkin`, `mgateway`, `mpatient`, `mnotes`, `mrisk`
- **Useful URLs**:
    - Actuator: [http://localhost:8090/actuator](http://localhost:8090/actuator)
    - Swagger UI: [http://localhost:8090/swagger-ui](http://localhost:8090/swagger-ui)

### Microservice `mpatient`
- **Role**: Patient data management (CRU).
- **Port**: 9001
- **Database**: MySQL (patientservice)
- **Features**: Create, read, update patients, data validation.
- **Dependencies**: `eureka-server`, `zipkin`, `mgateway`
- **Useful URLs**:
    - Actuator: [http://localhost:9001/actuator](http://localhost:9001/actuator)
    - Swagger UI: [http://localhost:9001/swagger-ui](http://localhost:9001/swagger-ui)

### Microservice `mnotes`
- **Role**: Patient notes management (CR).
- **Port**: 9002
- **Database**: MongoDB (prod_notes)
- **Features**: Create, read notes, data validation.
- **Dependencies**: `eureka-server`, `zipkin`, `mgateway`
- **Useful URLs**:
    - Actuator: [http://localhost:9002/actuator](http://localhost:9002/actuator)
    - Swagger UI: [http://localhost:9002/swagger-ui](http://localhost:9002/swagger-ui)

### Microservice `mrisk`
- **Role**: Type 2 diabetes risk analysis.
- **Port**: 9003
- **Database**: None
- **Features**: Risk level calculation, communication via Feign.
- **Dependencies**: `eureka-server`, `zipkin`, `mgateway`, `mpatient`, `mnotes`
- **Useful URLs**:
    - Actuator: [http://localhost:9003/actuator](http://localhost:9003/actuator)
    - Swagger UI: [http://localhost:9003/swagger-ui](http://localhost:9003/swagger-ui)

---

## 🛠 Common Prerequisites
- Java 21
- Spring Boot 3.5.8
- Spring Cloud 2025.0.0
- Maven
- Docker
- Environment Variables: Specific configuration for each microservice (Eureka, MySQL, MongoDB, etc.).

---

## 🐳 Running the application with Docker

### ⚙️ Prerequisites:
- Java 21
- Maven
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed.

### 📋 Deployment steps

#### 1️⃣ **Retrieve the projects**
* Clone the GitHub repository of the **MicroDiab** project: `git clone https://github.com/mail2juanb/microdiab.git`
* Clone the GitHub repository of the **mgateway** project: `git clone https://github.com/mail2juanb/MGateway.git`
* Clone the GitHub repository of the **eureka-server** project: `git clone https://github.com/mail2juanb/eureka-server.git`
* Clone the GitHub repository of the **clientui** project: `git clone https://github.com/mail2juanb/clientui.git`
* Clone the GitHub repository of the **mnotes** project: `git clone https://github.com/mail2juanb/Mnotes.git`
* Clone the GitHub repository of the **mrisk** project: `git clone https://github.com/mail2juanb/Mrisk.git`
* Clone the GitHub repository of the **mpatient** project: `git clone https://github.com/mail2juanb/Mpatient.git`
* Download the Zipkin Docker image; it is automatically used by docker-compose:
  * From DockerHub: **openzipkin/zipkin**
  * More information about Zipkin: https://zipkin.io/pages/quickstart

**Expected directory structure**
microdiab/  
│─ docker-compose.yml  
│─ README.fr.md  
│─ README.md  
├─ mgateway/  
├─ clientui/  
├─ mpatient/  
├─ mnotes/  
└─ mrisk/

#### 2️⃣ Generate the .jar files (if necessary)
If the `*.jar` files of the microservices are not present in the `target/` folder of each module, run the following commands to generate them:
* Move to the root directory of each microservice: `cd [your_path]\microdiab\[microservice_name]`
* Generate the *.jar files for all microservices: `mvn clean package verify`
  * For the `mnotes` microservice, a MongoDB database is required: `uri=mongodb://localhost:27017/test_notes`
  * If you want to skip tests: `mvn clean package -DskipTests`

#### 3️⃣ Building and starting Docker images
* Position the terminal in the root directory, ***microdiab/***, containing the `docker-compose.yml` file
* Build the Docker images for each service: `docker-compose build`
* Start all services in the background with the command: `docker-compose up -d`
* Once all services have started, go to the following address via your browser: **http://localhost:9010/clientui/home**  
    Recommended browser: **Microsoft Edge**. Version 144.0.3719.82 (Official version) (64-bit)

---


## 📊 Monitoring and Documentation
Each microservice exposes:
- **Actuator**: Metrics and health
- **Swagger UI**: Interactive API documentation (for back-end microservices)
- **Javadoc**: Java documentation
- **Zipkin**: Distributed traces ([http://localhost:9411/zipkin](http://localhost:9411/zipkin))

---

## 🚀 Main Endpoints
- **mgateway**: Dynamic routing to back-end microservices.
- **clientui**: `/home`, `/patients`, `/add`, `/update/{id}`
- **mpatient**: `/patients`, `/patients/{id}`
- **mnotes**: `/notes/{patId}`, `/notes`
- **mrisk**: `/risk/{patId}`

---

## 🔒 Security
- **Centralized Authentication**: via `mgateway` (Spring Security).
- **Security Context Propagation**: via HTTP headers (`X-Auth-Username`, `X-Auth-Roles`).
- **Internal Security**: Each microservice is independently secured.

---

## 📂 Docker Structure
- **Network**: `microdiab-network`
- **Volumes**:
    - `mysql-mgateway-data` (mgateway)
    - `mysql-mpatient-data` (mpatient)
    - `mongodb-mnotes-data` (mnotes)

---

## 📄 Additional Documentation
For more details, refer to the README files of each microservice.

---

## 🌱 Green Code Challenges and Improvement Opportunities for MicroDiab

Green Code (or Green Coding) is a software eco-design approach that aims to minimize the environmental footprint 
of software development, beyond simple functional or technical performance considerations. It involves designing and
implementing code that consumes less energy during execution (CPU, memory, I/O, network), while also reducing 
operational costs and the carbon impact associated with the widespread use of digital tools.

### 💡 Improvement Recommendations

#### 🎚️ Limiting Data Retrieval to What Is Strictly Necessary
Currently, the display of the patient list relies on retrieving all available information for each patient. 
From an eco-responsible perspective, it would have been preferable to load only the data strictly required 
for display purposes: last name, first name, gender, and date of birth. This approach helps reduce the volume 
of transferred data, memory consumption, and processing time. It also avoids a problematic side effect: 
if new columns are added to the Patients table, these additional data would automatically be retrieved and 
transferred, even though they are not required for the user interface.

#### 🗃️ Merging Strongly Coupled Microservices
The microservices mpatient, mnotes, and mrisk are highly interdependent:
* each note is associated with a patient,
* each patient has a risk report.

In this context, merging these services could be considered. This would make it possible to:
* reduce the number of inter-service network calls (less traffic, lower latency),
* decrease the number of Docker containers that need to be running,
* reduce overall CPU and memory consumption.

This reflection highlights that a microservices architecture, while functionally relevant, can have 
a significant energy cost if it is oversized.

#### 🗑️ Removing Unnecessary or Non-Requested Features
Some API operations, such as deleting a patient or updating and deleting notes, are not explicitly requested by the client. 
Their presence leads to:
* additional code to maintain and test,
* unnecessarily exposed endpoints.

Reducing these features would help lighten the codebase, limit unnecessary processing, and decrease server resource 
consumption, while improving the overall maintainability of the application.

#### 📉 Measuring the Energy Impact of the System
An effective Green Code approach primarily relies on measuring and objectively assessing the environmental impact of 
an application. Without indicators, it is difficult to identify the most energy-intensive components or to evaluate the
actual benefits of proposed optimizations. Several tools and initiatives can be used for this purpose:

* **GreenFrame.io** : GreenFrame evaluates the carbon footprint of a web application, both on the frontend and backend sides.
It analyzes energy consumption based on usage scenarios and provides indicators that can be used to compare different
versions of an application or measure the impact of technical improvements.
* **Scaphandre** : Scaphandre is a monitoring tool that measures the actual energy consumption of applications, 
Docker containers, or Linux systems. It makes it possible to associate electrical consumption with running processes, 
which is particularly relevant in a containerized architecture such as MicroDiab.
* **EcoIndex** : EcoIndex is an environmental performance indicator for web pages. It is based on factors such as 
page weight, number of network requests, and DOM complexity to produce an environmental score. 
Although mainly frontend-oriented, it helps raise awareness of the impact of interface design and unnecessary resource loading.
* **Creedengo / EcoCode** : The EcoCode initiative, notably supported by Creedengo, provides eco-design rules and 
best practices that can be integrated directly into code analysis tools such as SonarQube. These rules help identify 
energy-intensive or non-optimized code patterns (unnecessary loops, excessive object allocation, redundant processing),
facilitating a Green Code approach from the development phase onward.
* **Green Code Initiative** : The Green Code Initiative is a community-driven effort aimed at promoting more responsible
digital practices through reference frameworks, best practices, and feedback. It encourages a broader awareness of
the environmental impact of software by considering digital sobriety as a quality criterion in its own right.

Using these tools and frameworks would make it possible to accurately quantify the energy impact of the MicroDiab 
application, identify priority areas for improvement, and objectively evaluate gains in terms of energy consumption and
CO₂ emissions. Even without direct implementation in the project, this approach illustrates the importance of
maintaining a critical and responsible perspective on architectural and development choices.
