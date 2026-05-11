USE RaPizz;
-- Destruction de la base de données RaPizz et de toutes ses tables
-- Destruction de la table Livraison
ALTER TABLE Livraison DROP CONSTRAINT PK_Livraison;
ALTER TABLE Livraison DROP CONSTRAINT FK_Livraison_Vehicule;
ALTER TABLE Livraison DROP CONSTRAINT FK_Livraison_Livreur;
ALTER TABLE Livraison DROP CONSTRAINT FK_Livraison_Pizza;
ALTER TABLE Livraison DROP CONSTRAINT FK_Livraison_Client;
DROP TABLE IF EXISTS Livraison;
-- Destruction de la table utilise_dans
ALTER TABLE utilise_dans DROP CONSTRAINT PK_utilise_dans;
ALTER TABLE utilise_dans DROP CONSTRAINT FK_utilise_dans_Pizza;
ALTER TABLE utilise_dans DROP CONSTRAINT FK_utilise_dans_Ingredient;
DROP TABLE IF EXISTS utilise_dans;
-- Destruction de la table Vehicule
ALTER TABLE Vehicule DROP CONSTRAINT PK_Vehicule;
DROP TABLE IF EXISTS Vehicule;
-- Destruction de la table Livreur
ALTER TABLE Livreur DROP CONSTRAINT PK_Livreur;
DROP TABLE IF EXISTS Livreur;
-- Destruction de la table Ingredient
ALTER TABLE Ingredient DROP CONSTRAINT PK_Ingredient;
DROP TABLE IF EXISTS Ingredient;
-- Destruction de la table Pizza
ALTER TABLE Pizza DROP CONSTRAINT PK_Pizza;
DROP TABLE IF EXISTS Pizza;
-- Destruction de la table Client
ALTER TABLE Client DROP CONSTRAINT PK_Client;
DROP TABLE IF EXISTS Client;

