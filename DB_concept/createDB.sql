CREATE DATABASE IF NOT EXISTS RaPizz;
USE RaPizz;

CREATE TABLE Client(
    id_client INT,
    CONSTRAINT pk_Client PRIMARY KEY(id_client),
    nom VARCHAR(20) NOT NULL,
    prenom VARCHAR(20) NOT NULL,
    numero_de_telephone VARCHAR(10),
    solde DECIMAL(5,2)
);


CREATE TABLE Pizza(
    id_pizza INT,
    CONSTRAINT pk_Pizza PRIMARY KEY(id_pizza),
    nom VARCHAR(25) NOT NULL,
    prix_de_base DECIMAL(4,2)
);


CREATE TABLE Ingredient(
    id_ing INT,
    CONSTRAINT pk_Ingredient PRIMARY KEY(id_ing),
    nom VARCHAR(20) NOT NULL
);


CREATE TABLE Livreur(
    id_livreur INT,
    CONSTRAINT pk_Livreur PRIMARY KEY(id_livreur),
    nom VARCHAR(20) NOT NULL,
    prenom VARCHAR(20) NOT NULL
);


CREATE TABLE Vehicule(
    id_vehicule INT,
    CONSTRAINT pk_Vehicule PRIMARY KEY(id_vehicule),
    nom VARCHAR(50) NOT NULL,
    type_vehicule VARCHAR(8) NOT NULL,
    CONSTRAINT ck_Vehicule_type CHECK(type_vehicule IN ('moto', 'voiture'))
);


CREATE TABLE Livraison(
    idlivraison INT,
    CONSTRAINT pk_Livraison PRIMARY KEY(idlivraison),
    `date` DATETIME NOT NULL,
    temps INT,
    prix_facture DECIMAL(4,2),
    est_gratuite BOOLEAN,
    taille DOUBLE,
    id_vehicule INT,
    id_livreur INT,
    id_pizza INT,
    id_client INT,
    CONSTRAINT fk_Livraison_Vehicule FOREIGN KEY(id_vehicule) REFERENCES Vehicule(id_vehicule),
    CONSTRAINT fk_Livraison_Livreur FOREIGN KEY(id_livreur) REFERENCES Livreur(id_livreur),
    CONSTRAINT fk_Livraison_Pizza FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza),
    CONSTRAINT fk_Livraison_Client FOREIGN KEY(id_client) REFERENCES Client(id_client)
);


CREATE TABLE utilise_dans(
    id_pizza INT,
    id_ing INT,
    quantite INT,
    CONSTRAINT pk_utilise_dans PRIMARY KEY(id_pizza, id_ing),
    CONSTRAINT fk_utilise_dans_Pizza FOREIGN KEY(id_pizza) REFERENCES Pizza(id_pizza),
    CONSTRAINT fk_utilise_dans_Ingredient FOREIGN KEY(id_ing) REFERENCES Ingredient(id_ing)
);


DESCRIBE Client;
DESCRIBE Pizza;
DESCRIBE Ingredient;
DESCRIBE Livreur;
DESCRIBE Vehicule;
DESCRIBE Livraison;
DESCRIBE utilise_dans;