# RaPizz - Gestion d'une Entreprise de Pizzas à Domicile

## Structure du Projet

```
Project-RaPizz/
├── DB_concept/
│   ├── createDB.sql       # Création de la base de données
│   ├── insDB.sql          # Insertion des données
│   ├── clearDB.sql        # Suppression des données
│   ├── queries.sql        # Requêtes SQL
│   └── triggers.sql       # Triggers et procédures stockées
├── src/ra/pizz/
│   ├── model/             # Classes modèles (Client, Pizza, etc.)
│   ├── dao/               # Data Access Objects (JDBC)
│   ├── ui/                # Interface Swing
│   ├── util/              # Utilitaires (DBConnection)
│   ├── AppMain.java       # Point d'entrée principal
│   └── TestConnection.java # Test de connexion
└── mysql-connector-j-9.7.0.jar  # Driver JDBC MySQL
```

## Prérequis

1. **MySQL Server** installé et en cours d'exécution
2. **JDK 8+** (testé avec JDK 25.0.3)
3. **Driver JDBC** : `mysql-connector-j-9.7.0.jar` (inclus)

## Installation et Configuration

### 1. Créer la base de données

```bash
cd c:\Users\alexp\Desktop\E3\Project-RaPizz

# Créer la DB et les tables
mysql -u root < DB_concept\createDB.sql

# Insérer les données
mysql -u root RaPizz < DB_concept\insDB.sql

# Charger les triggers (optionnel)
mysql -u root RaPizz < DB_concept\triggers.sql

# Vérifier
mysql -u root RaPizz -e "SELECT COUNT(*) as pizza_count FROM Pizza;"
```

### 2. Modifier les credentials (si nécessaire)

Éditer `src/ra/pizz/util/DBConnection.java` :
```java
private static final String USER = "root";      // votre user MySQL
private static final String PASSWORD = "";      // votre password MySQL
```

### 3. Compiler l'application

```bash
# Créer le répertoire de sortie
mkdir bin

# Compiler tous les fichiers
javac -d bin -cp "mysql-connector-j-9.7.0.jar" ^
  src\ra\pizz\*.java ^
  src\ra\pizz\model\*.java ^
  src\ra\pizz\dao\*.java ^
  src\ra\pizz\ui\*.java ^
  src\ra\pizz\util\*.java
```

### 4. Exécuter l'application

```bash
# Test de connexion
java -cp "bin;mysql-connector-j-9.7.0.jar" ra.pizz.TestConnection

# Lancer l'application Swing
java -cp "bin;mysql-connector-j-9.7.0.jar" ra.pizz.AppMain
```

## Fonctionnalités

### 1. Menu
- Affiche toutes les pizzas avec leurs ingrédients
- Calcul automatique des prix pour les 3 tailles (naine, humaine, ogresse)

### 2. Fiche de Livraison
- Liste toutes les livraisons avec détails livreur/client/pizza
- Affiche les retards calculés (temps > 30 min)

### 3. Statistiques
- **Véhicules non utilisés** : Affiche les véhicules jamais utilisés
- **Commandes par client** : Nombre de pizzas par client
- **Moyenne des commandes** : Calcul de la moyenne
- **Clients au-dessus moyenne** : Affiche les meilleurs clients

## Architecture DAO

L'application utilise le pattern DAO avec JDBC :

- **PizzaMenuDAO** : Requête Menu
- **FicheLivraisonDAO** : Requête Fiche de Livraison
- **StatisticsDAO** : Toutes les statistiques

## Notes

- Le pilote JDBC MySQL est déjà inclus : `mysql-connector-j-9.7.0.jar`
- Tous les modèles sont immutables (pattern immuable)
- Les requêtes utilisent des prepared statements ou des statements sécurisés
- La connexion est fermée automatiquement (try-with-resources)

## Améliorations possibles

- Ajouter un système d'authentification
- Implémenter la gestion des commandes multiples
- Ajouter un système de facturation PDF
- Implémenter des notifications de retard
