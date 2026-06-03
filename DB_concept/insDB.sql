USE RaPizz;
-- Insertion de données pour RaPizz -- Gestion d'une entreprise de pizzas à domicile

-- Insertion des clients
INSERT INTO Client(id_client, nom, prenom, numero_de_telephone, solde)
    VALUES
    (1, "Dupont", "Marie", "0612345678", 150.50),
    (2, "Martin", "Jean", "0623456789", 200.00),
    (3, "Bernard", "Sophie", "0634567890", 75.25),
    (4, "Thomas", "Luc", "0645678901", 300.00),
    (5, "Robert", "Anna", "0656789012", 50.75),
    (6, "Petit", "Pierre", "0667890123", 180.00),
    (7, "Durand", "Claire", "0678901234", 120.50),
    (8, "Lefevre", "Nicolas", "0689012345", 250.00);


-- Insertion des ingrédients
INSERT INTO Ingredient(id_ing, nom)
    VALUES
    (1, "Sauce Tomate"),
    (2, "Fromage Mozzarella"),
    (3, "Jambon"),
    (4, "Pepperoni"),
    (5, "Champignons"),
    (6, "Oignons"),
    (7, "Poivron Rouge"),
    (8, "Olives"),
    (9, "Tomate Fraiche"),
    (10, "Basile"),
    (11, "Chevre"),
    (12, "Bacon"),
    (13, "Ananas"),
    (14, "Oeuf"),
    (15, "Crevettes");


-- Insertion des pizzas
INSERT INTO Pizza(id_pizza, nom, prix_de_base)
    VALUES
    (1, "Margherita", 11.99),
    (2, "Carnivore", 14.99),
    (3, "Vegetarienne", 12.49),
    (4, "Four Fromages", 13.99),
    (5, "Hawaii", 13.49),
    (6, "Seafood", 15.99),
    (7, "BBQ Bacon", 14.49),
    (8, "Classique Jambon", 12.99);


-- Insertion des relations pizza-ingrédients (utilise_dans)
INSERT INTO utilise_dans(id_pizza, id_ing, quantite)
    VALUES
    (1, 1, 1),     -- Margherita: Sauce Tomate
    (1, 2, 2),     -- Margherita: Mozzarella
    (1, 9, 2),     -- Margherita: Tomate Fraiche
    (1, 10, 1),    -- Margherita: Basile
    
    (2, 1, 1),     -- Carnivore: Sauce Tomate
    (2, 2, 2),     -- Carnivore: Mozzarella
    (2, 3, 3),     -- Carnivore: Jambon
    (2, 4, 3),     -- Carnivore: Pepperoni
    (2, 12, 2),    -- Carnivore: Bacon
    
    (3, 1, 1),     -- Vegetarienne: Sauce Tomate
    (3, 2, 2),     -- Vegetarienne: Mozzarella
    (3, 5, 2),     -- Vegetarienne: Champignons
    (3, 7, 2),     -- Vegetarienne: Poivron Rouge
    (3, 8, 1),     -- Vegetarienne: Olives
    
    (4, 1, 1),     -- Four Fromages: Sauce Tomate
    (4, 2, 2),     -- Four Fromages: Mozzarella
    (4, 11, 1),    -- Four Fromages: Chevre
    
    (5, 1, 1),     -- Hawaii: Sauce Tomate
    (5, 2, 2),     -- Hawaii: Mozzarella
    (5, 3, 2),     -- Hawaii: Jambon
    (5, 13, 2),    -- Hawaii: Ananas
    
    (6, 1, 1),     -- Seafood: Sauce Tomate
    (6, 2, 2),     -- Seafood: Mozzarella
    (6, 15, 3),    -- Seafood: Crevettes
    (6, 6, 1),     -- Seafood: Oignons
    
    (7, 1, 1),     -- BBQ Bacon: Sauce Tomate
    (7, 2, 2),     -- BBQ Bacon: Mozzarella
    (7, 12, 3),    -- BBQ Bacon: Bacon
    (7, 6, 1),     -- BBQ Bacon: Oignons
    
    (8, 1, 1),     -- Classique Jambon: Sauce Tomate
    (8, 2, 2),     -- Classique Jambon: Mozzarella
    (8, 3, 2);     -- Classique Jambon: Jambon


-- Insertion des livreurs
INSERT INTO Livreur(id_livreur, nom, prenom)
    VALUES
    (1, "Leclerc", "Antoine"),
    (2, "Moreau", "Benjamin"),
    (3, "Gauthier", "Olivier"),
    (4, "Vincent", "Thomas"),
    (5, "Leblanc", "Sebastien");


-- Insertion des véhicules
INSERT INTO Vehicule(id_vehicule, nom, type_vehicule)
    VALUES
    (1, "Renault Scenic Blanche", "voiture"),
    (2, "Peugeot 206 Grise", "voiture"),
    (3, "Vespa Scooter Verte", "moto"),
    (4, "Mobylette Noire", "moto"),
    (5, "Citroen C3 Rouge", "voiture"),
    (6, "Yamaha Scooter Noir", "moto"),
    (7, "Ford Transit Blanche", "voiture");


-- Insertion des livraisons
INSERT INTO Livraison(idlivraison, `date`, temps, prix_facture, est_gratuite, taille, id_vehicule, id_livreur, id_pizza, id_client)
    VALUES
    (1, "2025-11-18 19:30:00", 25, 11.99, FALSE, 1.0, 1, 1, 1, 1),
    (2, "2025-11-18 20:15:00", 32, 0.00, TRUE, 1.0, 2, 2, 2, 2),      -- Gratuite (retard > 30 min)
    (3, "2025-11-18 20:45:00", 28, 14.99, FALSE, 1.0, 3, 3, 5, 3),
    (4, "2025-11-19 19:00:00", 22, 12.49, FALSE, 1.0, 4, 4, 3, 4),
    (5, "2025-11-19 19:45:00", 35, 0.00, TRUE, 1.0, 1, 1, 8, 5),      -- Gratuite (retard > 30 min)
    (6, "2025-11-19 20:30:00", 27, 13.99, FALSE, 1.0, 2, 2, 4, 6),
    (7, "2025-11-20 19:15:00", 29, 11.99, FALSE, 1.0, 5, 5, 1, 2),
    (8, "2025-11-20 20:00:00", 33, 0.00, TRUE, 1.0, 3, 3, 7, 1),      -- Gratuite (retard > 30 min)
    (9, "2025-11-20 20:45:00", 24, 15.99, FALSE, 1.0, 4, 4, 6, 7),
    (10, "2025-11-21 19:30:00", 26, 13.49, FALSE, 1.0, 6, 2, 5, 8),
    (11, "2025-11-21 20:15:00", 31, 0.00, TRUE, 1.0, 1, 1, 2, 3),     -- Gratuite (retard > 30 min)
    (12, "2025-11-22 19:00:00", 23, 14.49, FALSE, 1.0, 2, 3, 7, 4),
    (13, "2025-11-22 19:45:00", 28, 12.99, FALSE, 1.0, 5, 4, 8, 1),
    (14, "2025-11-22 20:30:00", 25, 11.99, FALSE, 1.0, 3, 5, 1, 6),
    (15, "2025-11-23 19:30:00", 36, 0.00, TRUE, 1.0, 4, 1, 3, 2),     -- Gratuite (retard > 30 min et fidélité)
    (16, "2025-11-23 20:15:00", 27, 12.49, FALSE, 1.0, 6, 2, 3, 7),
    (17, "2025-11-24 19:00:00", 24, 13.99, FALSE, 1.0, 1, 3, 4, 8),
    (18, "2025-11-24 19:45:00", 30, 14.99, FALSE, 1.0, 2, 4, 2, 5),     -- Gratuite (retard >= 30 min)
    (19, "2025-11-24 20:30:00", 26, 15.99, FALSE, 1.0, 5, 5, 6, 3),
    (21, "2025-11-24 21:15:00", 24, 11.99, FALSE, 1.0, 1, 1, 1, 4),
    (22, "2025-11-24 22:00:00", 27, 12.49, FALSE, 1.0, 2, 2, 3, 4),
    (23, "2025-11-25 10:30:00", 25, 13.99, FALSE, 1.0, 3, 3, 4, 4),
    (24, "2025-11-25 12:15:00", 28, 14.49, FALSE, 1.0, 5, 4, 7, 4),
    (25, "2025-11-25 15:45:00", 26, 13.49, FALSE, 1.0, 6, 5, 5, 4),
    (26, "2025-11-25 18:30:00", 29, 15.99, FALSE, 1.0, 4, 1, 6, 4),
    (27, "2025-11-25 19:15:00", 29, 11.99, FALSE, 1.0, 3, 1, 1, 4),
    (28, "2025-11-26 19:15:00", 25, 11.99, FALSE, 1.0, 3, 1, 1, 4),
    (20, "2025-11-29 19:15:00", 10, 0.00, TRUE, 1.0, 3, 1, 1, 4);     -- Gratuite (10ème pizza achetée - fidélité)

    