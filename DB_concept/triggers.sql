-- Procédure stockée pour vérifier et appliquer les bonifications
USE RaPizz;

DROP TRIGGER IF EXISTS tr_pizza_gratuite;
DROP TRIGGER IF EXISTS tr_pizza_gratuite_10;
DROP TRIGGER IF EXISTS tr_pizza_gratuite_retard;
DELIMITER //

CREATE TRIGGER tr_pizza_gratuite
BEFORE INSERT ON Livraison
FOR EACH ROW
BEGIN
    DECLARE pizza_count INT;

    -- Compte les pizzas déjà achetées par ce client (hors commande en cours)
    SELECT COUNT(*) INTO pizza_count
    FROM Livraison
    WHERE id_client = NEW.id_client;

    -- Gratuité fidélité : toutes les 10 pizzas (9e, 19e, 29e...)
    -- Gratuité retard  : livraison > 30 minutes
    IF MOD(pizza_count, 10) = 9 OR NEW.temps > 30 THEN
        SET NEW.prix_facture  = 0;
        SET NEW.est_gratuite  = TRUE;
    END IF;
END //
DELIMITER;