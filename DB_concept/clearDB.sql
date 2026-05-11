USE RaPizz;

-- Suppression des contraintes étrangères
ALTER TABLE Livraison DROP CONSTRAINT fk_Livraison_Vehicule;
ALTER TABLE Livraison DROP CONSTRAINT fk_Livraison_Livreur;
ALTER TABLE Livraison DROP CONSTRAINT fk_Livraison_Pizza;
ALTER TABLE Livraison DROP CONSTRAINT fk_Livraison_Client;

ALTER TABLE utilise_dans DROP CONSTRAINT fk_utilise_dans_Pizza;
ALTER TABLE utilise_dans DROP CONSTRAINT fk_utilise_dans_Ingredient;

-- Suppression des tables
DROP TABLE Livraison;
DROP TABLE utilise_dans;
DROP TABLE Pizza;
DROP TABLE Ingredient;
DROP TABLE Client;
DROP TABLE Livreur;
DROP TABLE Vehicule;

