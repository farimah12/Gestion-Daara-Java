# 🕌 Gestion Daara

Application desktop (Java Swing) de gestion d'une Daara (école coranique) : maîtres, classes, talibés (élèves) et suivi de leur progression dans la mémorisation du Coran.

Projet réalisé par **Adji Farimata CISSE**, étudiante en Génie Logiciel à l'Institut Supérieur d'Informatique (ISI), Dakar — dans le cadre d'un examen (L2 Génie Logiciel).

---

## ✨ Fonctionnalités

- 👳 Gestion des maîtres (matricule, nom, téléphone)
- 🏷️ Gestion des classes (code, libellé, niveau : Débutant / Intermédiaire / Avancé), rattachées à un maître
- 🧒 Gestion des talibés (élèves) : matricule, identité, date de naissance, tuteur et contact du tuteur, rattachés à une classe
- 📖 Suivi de la progression de mémorisation : sourate étudiée, nombre de versets, date d'évaluation, appréciation
- 📤 Export des données au format CSV
- ⚠️ Gestion d'erreurs métier via des exceptions personnalisées (classe/maître/talibé déjà existant ou introuvable, suppression impossible, progression invalide...)
- 🎨 Interface graphique moderne avec FlatLaf

## 🛠️ Technologies utilisées

- **Java 21**
- **Hibernate ORM** (JPA) — mapping objet-relationnel
- **MySQL** (via MySQL Connector/J)
- **Swing** + **FlatLaf** (interface graphique)
- **Lombok**
- **Maven**

## 🏗️ Architecture

Le projet suit une architecture en couches (MVC) :

```
daara/
├── src/main/java/sn/l2gl/farimah/daara/
│   ├── App.java                  # Point d'entrée
│   ├── model/
│   │   ├── models/               # Entités JPA : Maitre, Classe, Talibe, Progression
│   │   └── dao/                  # Accès aux données (DAO génériques par entité)
│   ├── controller/                # Logique métier : ClasseController, MaitreController,
│   │                               #   TalibeController, ProgressionController
│   ├── view/                      # Interface Swing : AppDaara, ClasseView, MaitreView,
│   │                               #   TalibeView, ProgressionView
│   ├── exception/                 # Exceptions métier personnalisées
│   └── util/                      # CsvExporter, HibernateUtil
├── src/main/resources/
│   └── hibernate.cfg.xml          # Configuration de la base de données
└── pom.xml
```

## 🚀 Installation locale

### Prérequis
- Java 21
- Maven
- MySQL / MariaDB

### Étapes

1. Clone ce dépôt :
```bash
git clone https://github.com/farimah12/NOM-DU-REPO.git
cd NOM-DU-REPO/daara
```

2. Crée une base de données vide nommée `daara` dans MySQL :
```sql
CREATE DATABASE daara;
```

> 💡 Pas besoin d'importer de script SQL : grâce à Hibernate (`hibernate.hbm2ddl.auto = update`), les tables sont créées et mises à jour automatiquement au premier lancement, à partir des entités Java.

3. Vérifie les identifiants de connexion dans `src/main/resources/hibernate.cfg.xml` (par défaut : utilisateur `root`, sans mot de passe, port `3306`).

4. Compile et lance l'application :
```bash
mvn clean install
mvn exec:java -Dexec.mainClass="sn.l2gl.farimah.daara.App"
```
*(ou lance simplement la classe `App.java` depuis IntelliJ IDEA / ton IDE)*

## 👩‍💻 Auteure

**Adji Farimata CISSE**
Étudiante en Génie Logiciel — ISI, Dakar
📧 adjifarimahcisse@gmail.com
