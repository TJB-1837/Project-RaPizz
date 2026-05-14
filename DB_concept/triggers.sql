-- Procédure stockée pour vérifier et appliquer les bonifications
USE RaPizz;

DELIMITER //

-- Trigger pour appliquer la bonification de 10 pizzas
CREATE TRIGGER tr_pizza_gratuite_10 
AFTER INSERT ON Livraison
FOR EACH ROW
BEGIN
    DECLARE pizza_count INT;
    DECLARE pizza_id INT;
    DECLARE pizza_price DECIMAL(4,2);
    
    -- Compter les pizzas du client
    SELECT COUNT(*) INTO pizza_count FROM Livraison WHERE id_client = NEW.id_client;
    
    -- Si le client a acheté exactement 10 pizzas (ou multiple de 10)
    IF MOD(pizza_count, 10) = 0 THEN
        -- Créer une livraison gratuite automatique (pizza aléatoire)
        SELECT id_pizza, prix_de_base INTO pizza_id, pizza_price
        FROM Pizza 
        ORDER BY RAND() 
        LIMIT 1;
        
        INSERT INTO Livraison (idlivraison, date, temps, prix_facture, est_gratuite, taille, 
                               id_vehicule, id_livreur, id_pizza, id_client)
        VALUES ((SELECT MAX(idlivraison) + 1 FROM (SELECT MAX(idlivraison) as idlivraison FROM Livraison) t),
                NOW(), 0, 0, TRUE, NEW.taille, NEW.id_vehicule, NEW.id_livreur, pizza_id, NEW.id_client);
    END IF;
END //

-- Trigger pour appliquer la bonification de retard (>30 min)
CREATE TRIGGER tr_pizza_gratuite_retard
BEFORE UPDATE ON Livraison
FOR EACH ROW
BEGIN
    IF NEW.temps > 30 AND OLD.temps <= 30 THEN
        SET NEW.prix_facture = 0;
        SET NEW.est_gratuite = TRUE;
    END IF;
END //

DELIMITER ;
