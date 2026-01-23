[🇫🇷 Lire en français](README.fr.md) | [🇬🇧 Read in English](README.md)

# MicroDiab – Résumé des Microservices

---

**Projet** : MicroDiab  
**Organisation** : MediLabo Solutions  
**Auteur** : michaudj  
**Date** : Janvier 2026  
**Version** : 1.0.0

---

## 📌 Vue d'ensemble
L'application **MicroDiab** est une solution microservices pour l'analyse et la gestion du diabète. 
Elle est composée de plusieurs microservices, chacun ayant un rôle spécifique, et utilise **Spring Boot 3.5.8**, 
**Java 21**, **Spring Cloud 2025.0.0**, **Docker**, et des bases de données MySQL et MongoDB.

### 🌐 Architecture Globale
- **Découverte de services** : Eureka Server
- **Traçage distribué** : `zipkin`
- **Sécurité** : Authentification centralisée via `mgateway`
- **Communication** : Feign Client, Spring Cloud Gateway
- **Containerisation** : Docker (réseau partagé : `microdiab-network`)

---

## 📦 Liste des Microservices

### Microservice `mgateway`
- **Rôle** : Passerelle API centrale, routage dynamique, authentification centralisée, traçage distribué.
- **Port** : 9010
- **Base de données** : MySQL R2DBC (userservice)
- **Fonctionnalités** : Routage dynamique via Eureka, sécurité Spring Security, propagation des headers de sécurité, 
documentation Swagger UI.
- **Dépendances** : `eureka-server`, `zipkin`, `mpatient`, `mnotes`, `mrisk`, `clientui`
- **URLs utiles** :
    - Actuator : [http://localhost:9010/actuator](http://localhost:9010/actuator)
    - Zipkin : [http://localhost:9411/zipkin](http://localhost:9411/zipkin)
    - Javadoc : [http://localhost:9010/apidocs](http://localhost:9010/apidocs)

### Microservice `clientui`
- **Rôle** : Microservice front-end, affichage des interfaces utilisateur via Thymeleaf.
- **Port** : 8090
- **Base de données** : Aucune
- **Fonctionnalités** : Affichage des patients, notes médicales, niveau de risque de diabète, communication via Feign.
- **Dépendances** : `eureka-server`, `zipkin`, `mgateway`, `mpatient`, `mnotes`, `mrisk`
- **URLs utiles** :
    - Actuator : [http://localhost:8090/actuator](http://localhost:8090/actuator)
    - Swagger UI : [http://localhost:8090/swagger-ui](http://localhost:8090/swagger-ui)

### Microservice `mpatient`
- **Rôle** : Gestion des données des patients (CRU).
- **Port** : 9001
- **Base de données** : MySQL (patientservice)
- **Fonctionnalités** : Création, lecture, mise à jour de patients, validation des données.
- **Dépendances** : `eureka-server`, `zipkin`, `mgateway`
- **URLs utiles** :
    - Actuator : [http://localhost:9001/actuator](http://localhost:9001/actuator)
    - Swagger UI : [http://localhost:9001/swagger-ui](http://localhost:9001/swagger-ui)

### Microservice `mnotes`
- **Rôle** : Gestion des notes des patients (CR).
- **Port** : 9002
- **Base de données** : MongoDB (prod_notes)
- **Fonctionnalités** : Création, lecture de notes, validation des données.
- **Dépendances** : `eureka-server`, `zipkin`, `mgateway`
- **URLs utiles** :
    - Actuator : [http://localhost:9002/actuator](http://localhost:9002/actuator)
    - Swagger UI : [http://localhost:9002/swagger-ui](http://localhost:9002/swagger-ui)

### Microservice `mrisk`
- **Rôle** : Analyse du risque de diabète de type 2.
- **Port** : 9003
- **Base de données** : Aucune
- **Fonctionnalités** : Calcul du niveau de risque, communication via Feign.
- **Dépendances** : `eureka-server`, `zipkin`, `mgateway`, `mpatient`, `mnotes`
- **URLs utiles** :
    - Actuator : [http://localhost:9003/actuator](http://localhost:9003/actuator)
    - Swagger UI : [http://localhost:9003/swagger-ui](http://localhost:9003/swagger-ui)

---

## 🛠 Prérequis Communs
- Java 21
- Spring Boot 3.5.8
- Spring Cloud 2025.0.0
- Maven
- Docker
- Variables d'environnement : Configuration spécifique pour chaque microservice (Eureka, MySQL, MongoDB, etc.).

---

## 🐳 Lancement de l’application avec Docker

### ⚙️ Prérequis : 
- Java 21
- Maven
- [Docker](https://www.docker.com/) et [Docker Compose](https://docs.docker.com/compose/) installés.

### 📋 Étapes de déploiement

#### 1️⃣ **Récupérer le projet**
* Cloner le dépôt GitHub du projet **MicroDiab** : `git clone https://github.com/mail2juanb/microdiab.git`
###### **Récupérer les projets indépendants**
* Cloner le dépôt GitHub du projet **mgateway** : `git clone https://github.com/mail2juanb/MGateway.git`
* Cloner le dépôt GitHub du projet **eureka-server** : `git clone https://github.com/mail2juanb/eureka-server.git`
* Cloner le dépôt GitHub du projet **clientui** : `git clone https://github.com/mail2juanb/clientui.git`
* Cloner le dépôt GitHub du projet **mnotes** : `git clone https://github.com/mail2juanb/Mnotes.git`
* Cloner le dépôt GitHub du projet **mrisk** : `git clone https://github.com/mail2juanb/Mrisk.git`
* Cloner le dépôt GitHub du projet **mpatient** : `git clone https://github.com/mail2juanb/Mpatient.git`
* **Télécharger l'image docker de zipkin, l’image est automatiquement utilisée par docker-compose :**
  * A partir de DockerHub : **openzipkin/zipkin**
  * Plus d'infos sur Zipkin : https://zipkin.io/pages/quickstart

**Arborescence souhaitée**
microdiab/  
│─ docker-compose.yml  
│─ README.fr.md  
│─ README.md  
├─ mgateway/  
├─ clientui/  
├─ mpatient/  
├─ mnotes/  
└─ mrisk/  

#### 2️⃣ Générer les fichiers .jar (si nécessaire)
Si les fichiers `*.jar` des microservices ne sont pas présents dans le dossier target/ de chaque module, exécutez les commandes suivantes pour les générer :
* Se placer à la racine de chaque microservice : `cd [votre_chemin]\microdiab\[nom_du_microservice]`
* Générer les fichiers *.jar pour tous les microservices : `mvn clean package verify`
  * Pour le microservice `mnotes`, il faudra une base de données MongoDB : `uri=mongodb://localhost:27017/test_notes`
  * Si vous souhaitez exclure les tests : `mvn clean package -DskipTests`

#### 3️⃣ Construire et démarrer les images Docker
* Placer le terminal sur le dossier racine, ***microdiab/***, contenant le fichier `docker-compose.yml`
* Lancez la construction des images Docker pour chaque service : `docker-compose build`
* Lancez tous les services en arrière-plan avec la commande : `docker-compose up -d`
* Une fois l'ensemble des services démarrés, rendez-vous à l'adresse suivante via votre navigateur : **http://localhost:9010/clientui/home**  
  Navigateur recommandé : **Microsoft Edge**. Version 144.0.3719.82 (Version officielle) (64 bits)
---

## 📊 Monitoring et Documentation
Chaque microservice expose :
- **Actuator** : Métriques et santé
- **Swagger UI** : Documentation API interactive (pour les microservices back)
- **Javadoc** : Documentation Java
- **Zipkin** : Traces distribuées ([http://localhost:9411/zipkin](http://localhost:9411/zipkin))

---

## 🚀 Endpoints Principaux
- **mgateway** : Routage dynamique vers les microservices back.
- **clientui** : `/home`, `/patients`, `/add`, `/update/{id}`
- **mpatient** : `/patients`, `/patients/{id}`
- **mnotes** : `/notes/{patId}`, `/notes`
- **mrisk** : `/risk/{patId}`

---

## 🔒 Sécurité
- **Authentification centralisée** via `mgateway` (Spring Security).
- **Propagation du contexte de sécurité** via headers HTTP (`X-Auth-Username`, `X-Auth-Roles`).
- **Sécurité interne** : Chaque microservice est sécurisé indépendamment.

---

## 📂 Structure Docker
- **Réseau** : `microdiab-network`
- **Volumes** :
    - `mysql-mgateway-data` (mgateway)
    - `mysql-mpatient-data` (mpatient)
    - `mongodb-mnotes-data` (mnotes)

---

## 📄 Documentation Complémentaire
Pour plus de détails, consulter les fichiers README de chaque microservice.

---

## 🌱 Enjeux du _Green Code_ et pistes d’amélioration pour Microdiab

Le _Green Code_ (ou _Green Coding_) est une démarche d’éco-conception logicielle qui vise à 
**minimiser l’empreinte environnementale du développement logiciel**, au-delà de la simple fonctionnalité ou 
performance technique. Il s’agit de concevoir et implémenter du code qui consomme moins d’énergie durant 
son exécution (CPU, mémoire, I/O, réseau), tout en réduisant les coûts opérationnels et l’impact carbone associé 
à l’usage massif des outils numériques.  

### 💡 Recommandations d’améliorations

#### 🎚️ Limiter les données récupérées au strict nécessaire
Actuellement, l’affichage de la liste des patients repose sur la récupération de l’ensemble des informations d’un patient.
D’un point de vue éco-responsable, il aurait été préférable de ne charger que les données réellement utiles 
à l’affichage : nom, prénom, genre et date de naissance.  
Cette approche permet de réduire le volume de données transférées, la consommation mémoire et le temps de traitement. 
Elle évite également un effet de bord problématique : si de nouvelles colonnes sont ajoutées à la table des Patients, 
celles-ci seront automatiquement récupérées et transférées alors qu’elles ne sont pas nécessaires à l’interface.

#### 🗃️ Fusionner les microservices fortement couplés
Les microservices mpatient, mnotes et mrisk sont fortement interdépendants :  
* chaque note est associée à un patient.
* chaque patient dispose d’un rapport de risque.

Dans ce contexte, une fusion de ces services pourrait être envisagée. Elle permettrait de :
* réduire le nombre d’appels réseau inter-services (moins de trafic, moins de latence).
* diminuer le nombre de conteneurs Docker à maintenir en fonctionnement.
* réduire la consommation globale de CPU et de mémoire.  

Cette réflexion montre qu’une architecture microservices, bien que pertinente fonctionnellement, peut avoir un coût 
énergétique non négligeable si elle est surdimensionnée.

#### 🗑️ Supprimer les fonctionnalités inutiles ou non demandées
Certaines opérations exposées par l’API, comme la suppression d’un patient ou la modification et suppression des notes, 
ne sont pas explicitement demandées par le client. Leur présence entraîne :
* du code supplémentaire à maintenir et à tester.
* des endpoints exposés inutilement.

Réduire ces fonctionnalités permettrait d’alléger la base de code, de limiter les traitements inutiles et de diminuer 
la consommation de ressources serveur, tout en améliorant la maintenabilité globale de l’application.

#### 📉 Mesurer l’impact énergétique du système
Une démarche Green Code efficace repose avant tout sur la mesure et l’objectivation de l’impact environnemental 
d’une application. Sans indicateurs, il est difficile d’identifier les points les plus énergivores ou d’évaluer 
les bénéfices réels des optimisations envisagées. Plusieurs outils et initiatives peuvent être mobilisés dans cette optique :

* **GreenFrame.io** : GreenFrame permet d’évaluer l’empreinte carbone d’une application web, aussi bien côté frontend 
que backend. Il analyse la consommation énergétique liée aux scénarios d’utilisation et fournit des indicateurs 
exploitables pour comparer différentes versions d’une application ou mesurer l’impact d’améliorations techniques.
* **Scaphandre** : Scaphandre est un outil de monitoring qui mesure la consommation énergétique réelle des applications,
conteneurs Docker ou systèmes Linux. Il permet d’associer une consommation électrique aux processus exécutés, 
ce qui est particulièrement pertinent dans une architecture conteneurisée comme celle de MicroDiab.
* **EcoIndex** : EcoIndex est un indicateur de performance environnementale des pages web. Il s’appuie notamment sur 
le poids des pages, le nombre de requêtes réseau et la complexité du DOM pour fournir une note environnementale. 
Bien que principalement orienté frontend, il permet de sensibiliser aux impacts liés à la conception des interfaces 
et aux ressources chargées inutilement.
* **Creedengo / EcoCode** : L’initiative EcoCode, portée notamment par Creedengo, propose des règles et bonnes pratiques
d’éco-conception logicielle intégrables directement dans les outils d’analyse de code (comme SonarQube). 
Ces règles permettent d’identifier des patterns de code énergivores ou non optimisés (boucles inutiles, allocations
excessives, traitements superflus), facilitant ainsi une approche Green Code dès la phase de développement.
* **Green Code Initiative** : La Green Code Initiative est une démarche communautaire visant à promouvoir un numérique 
plus responsable à travers des référentiels, des bonnes pratiques et des retours d’expérience. 
Elle encourage une prise de conscience globale de l’impact environnemental du logiciel, en intégrant la sobriété 
numérique comme un critère de qualité à part entière.

L’utilisation de ces outils et référentiels permettrait de quantifier précisément l’impact énergétique 
de l’application MicroDiab, d’identifier les zones d’amélioration prioritaires et d’évaluer objectivement 
les gains en termes de consommation d’énergie et d’émissions de CO₂. Même sans mise en œuvre directe dans le projet, 
cette démarche illustre l’importance d’un regard critique et responsable sur les choix d’architecture et de développement.