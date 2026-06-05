# RaPizz — Gestion d'une entreprise de pizzas à domicile

## Auteurs
Antoine MARMOL
Alex PITOLEX

## Vue d'ensemble
Ce projet Java (Swing + JDBC) gère le menu, les commandes et les fiches de livraison
pour une pizzeria. La connexion à une base MySQL est assurée via `mysql-connector-j-9.7.0.jar`.

## Arborescence principale

```
Project-RaPizz/
├── DB_concept/                # Scripts SQL (création, insertion, triggers, requêtes)
├── src/                      # Code source Java (package `ra.pizz`)
├── bin/                      # Fichiers compilés (générés après compilation)
└── mysql-connector-j-9.7.0.jar# Driver JDBC MySQL
```

## Prérequis

- MySQL Server installé et en cours d'exécution
- JDK 8 ou supérieur
- `mysql-connector-j-9.7.0.jar` (fourni dans le dépôt)

## Installation rapide (Windows)

1. Créer la base et les tables :

Dans le CLI MySQL exécutez les commandes suivantes (avec chemin absolu) :
SOURCE path\Project-RaPizz\DB_concept\clearDB.sql
SOURCE path\Project-RaPizz\DB_concept\createDB.sql
SOURCE path\Project-RaPizz\DB_concept\insDB.sql
SOURCE path\Project-RaPizz\DB_concept\triggers.sql

2. Ajuster les identifiants MySQL si nécessaire et créez la base de données :

Éditez `src/rapizz/util/DBConnection.java` et mettez à jour `USER` et `PASSWORD`.


Votre base de donnée est prête pour l'utilisation.

4. lancer l'application

Exécutez le fichier run.bat (./run.bat en console) pour lancer l'application.
Ce fichier compile le projet puis donne les 2 choix suivants :

Choix 1 : tester la connextion à la base de données

Choix 2 : lancement de l'application


## Fonctionnalités principales

- Affichage du menu et des ingrédients
- Calcul automatique des prix selon la taille
- Gestion des fiches de livraison (retards détectés)
- Statistiques (véhicules non utilisés, commandes par client, clients au-dessus de la moyenne)

## Architecture

Le projet utilise le pattern DAO avec JDBC. Principaux DAO :

- `PizzaMenuDAO`, `FicheLivraisonDAO`, `StatisticsDAO`, etc.

## Fichiers utiles

- Scripts SQL : [DB_concept/createDB.sql](DB_concept/createDB.sql)
- Test de connexion : [src/ra/pizz/TestConnection.java](src/ra/pizz/TestConnection.java)
- Point d'entrée GUI : [src/ra/pizz/AppMain.java](src/ra/pizz/AppMain.java)


## Vidéo de présentation 

Vous trouverez la vidéo de présentaion grâce au lien suivant : https://youtu.be/zugWiLksGEA



