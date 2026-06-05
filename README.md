# RaPizz — Gestion d'une entreprise de pizzas à domicile

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

```powershell
mysql -u root < DB_concept\createDB.sql
mysql -u root RaPizz < DB_concept\insDB.sql
# (optionnel) charger les triggers
mysql -u root RaPizz < DB_concept\triggers.sql
```

2. Ajuster les identifiants MySQL si nécessaire :

Éditez `src/ra/pizz/util/DBConnection.java` et mettez à jour `USER` et `PASSWORD`.

3. Compiler le projet :

```powershell
mkdir bin
javac -d bin -cp "mysql-connector-j-9.7.0.jar" src\ra\pizz\*.java src\ra\pizz\model\*.java src\ra\pizz\dao\*.java src\ra\pizz\ui\*.java src\ra\pizz\util\*.java
```

4. Lancer l'application ou tester la connexion :

```powershell
java -cp "bin;mysql-connector-j-9.7.0.jar" ra.pizz.TestConnection
java -cp "bin;mysql-connector-j-9.7.0.jar" ra.pizz.AppMain
```

Remarque : sur Linux/macOS, remplacez `;` par `:` dans le classpath.

## Fonctionnalités principales

- Affichage du menu et des ingrédients
- Calcul automatique des prix selon la taille
- Gestion des fiches de livraison (retards détectés)
- Statistiques (véhicules non utilisés, commandes par client, clients au-dessus de la moyenne)

## Architecture

Le projet utilise le pattern DAO avec JDBC. Principaux DAO :

- `PizzaMenuDAO`, `FicheLivraisonDAO`, `StatisticsDAO`, etc.

## Conseils d'utilisation

- Exécutez d'abord le script `triggers.sql` si vous utilisez les fonctionnalités dépendantes des triggers.
- Vérifiez les credentials dans `src/ra/pizz/util/DBConnection.java` avant d'exécuter l'application.

## Fichiers utiles

- Scripts SQL : [DB_concept/createDB.sql](DB_concept/createDB.sql)
- Test de connexion : [src/ra/pizz/TestConnection.java](src/ra/pizz/TestConnection.java)
- Point d'entrée GUI : [src/ra/pizz/AppMain.java](src/ra/pizz/AppMain.java)

## Améliorations possibles

- Authentification et gestion des utilisateurs
- Gestion de commandes multiples et facturation (PDF)
- Notifications (email/SMS) pour les retards

---

Si vous voulez, je peux :

- exécuter une compilation et tester `TestConnection` ici, ou
- ajouter des instructions spécifiques pour Linux/macOS.

