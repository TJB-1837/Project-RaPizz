CREATE DATABASE IF NOT EXISTS RaPizz;
USE RaPizz;
CREATE TABLE Client(
   id_client INT,
   nom VARCHAR(20) NOT NULL,
   prenom VARCHAR(20) NOT NULL,
   numero_de_telephone VARCHAR(10),
   solde DECIMAL(5,2),
   CONSTRAINT PK_Client PRIMARY KEY(id_client)
);

CREATE TABLE Pizza(
   id_pizza INT,
   nom VARCHAR(25) NOT NULL,
   prix_de_base DECIMAL(4,2),
   CONSTRAINT PK_Pizza PRIMARY KEY(id_pizza)
);

CREATE TABLE Ingredient(
   id_ing INT,
   nom VARCHAR(20) NOT NULL,
   CONSTRAINT PK_Ingredient PRIMARY KEY(id_ing)
);

CREATE TABLE Livreur(
   id_livreur INT,
   nom VARCHAR(20) NOT NULL,
   prenom VARCHAR(20) NOT NULL,
   CONSTRAINT PK_Livreur PRIMARY KEY(id_livreur)
);

CREATE TABLE Vehicule(
   id_vehicule INT,
   nom VARCHAR(50) NOT NULL,
   type_vehicule VARCHAR(8) NOT NULL,
   CONSTRAINT ck_Vehicule_type CHECK(type_vehicule IN ('moto', 'voiture')),
   CONSTRAINT PK_Vehicule PRIMARY KEY(id_vehicule)
);

CREATE TABLE Livraison(
   id_livraison INT,
   date_ DATETIME NOT NULL,
   temps INT,
   prix_facture DECIMAL(4,2),
   est_gratuite BOOLEAN,
   taille DOUBLE,
   id_vehicule INT NOT NULL,
   id_livreur INT NOT NULL,
   id_pizza INT NOT NULL,
   id_client INT NOT NULL,
   CONSTRAINT PK_Livraison PRIMARY KEY(id_livraison),
   CONSTRAINT FK_Livraison_Vehicule FOREIGN KEY(id_vehicule) REFERENCES Vehicule(id_vehicule),
   CONSTRAINT FK_Livraison_Livreur FOREIGN KEY(id_livreur) REFERENCES Livreur(id_livreur),
   CONSTRAINT FK_Livraison_Pizza FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza),
   CONSTRAINT FK_Livraison_Client FOREIGN KEY(id_client) REFERENCES Client(id_client)
);

CREATE TABLE utilise_dans(
   id_pizza INT,
   id_ing INT,
   quantite INT,
   CONSTRAINT PK_utilise_dans PRIMARY KEY(id_pizza, id_ing),
   CONSTRAINT FK_utilise_dans_Pizza FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza),
   CONSTRAINT FK_utilise_dans_Ingredient FOREIGN KEY(id_ing) REFERENCES Ingredient(id_ing)
);

DESCRIBE Client;
DESCRIBE Pizza;
DESCRIBE Ingredient;
DESCRIBE Livreur;
DESCRIBE Vehicule;
DESCRIBE Livraison;
DESCRIBE utilise_dans;