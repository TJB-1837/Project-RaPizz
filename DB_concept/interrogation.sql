USE RaPizz;
-- 1) Retrait du menu de la base de données
SELECT p.nom, p.prix_de_base, i.nom FROM pizza p, ingredient i, utilise_dans u WHERE p.id_pizza = u.id_pizza AND u.id_ing = i.id_ing;
-- Requête 2) Fiche de livraison
-- Affiche pour chaque livraison : livreur, type de véhicule, client, date, retard, pizza et prix
select l.nom as "Nom Livreur", l.prenom as "Prenom Livreur", v.type_vehicule, c.nom AS "Nom client", c.prenom as "Prenom Client", liv.date_,
             case when liv.temps > 30 then liv.temps - 30 else 0 end as retard,
             p.nom, p.prix_de_base
from Livreur l, Vehicule v, Client c, Livraison liv, Pizza p
where liv.id_livreur = l.id_livreur and liv.id_vehicule = v.id_vehicule 
  and liv.id_client = c.id_client and liv.id_pizza = p.id_pizza
order by liv.date_;


-- Requête 3a) Véhicules n'ayant jamais servi
-- Affiche les véhicules qui n'ont jamais été utilisés pour une livraison
select v.nom
from Vehicule v
where v.id_vehicule not in(select distinct id_vehicule from Livraison);


-- Requête 3b) Nombre de commandes par client
select c.nom, c.prenom, count(*) as "Nombre de pizzas commandees"
from Client c, Livraison liv
where c.id_client = liv.id_client
group by c.id_client
order by c.nom;


-- Requête 3c) Moyenne des commandes
select AVG(nb_pizzas) as "Nombre moyen de pizzas par client"
from (
    select c.id_client, count(*) as nb_pizzas
    from Client c, Livraison liv
    where c.id_client = liv.id_client
    group by c.id_client
) as commandes_par_client;


-- Requête 3d) Clients ayant commandé plus que la moyenne
select c.nom, c.prenom, count(*) as "Nombre de pizzas"
from Client c, Livraison liv
where c.id_client = liv.id_client
group by c.id_client
having count(*) > (
    select AVG(nb_pizzas)
    from (
        select count(*) as nb_pizzas
        from Livraison
        group by id_client
    ) as avg_pizzas
)
order by count(*) desc;
