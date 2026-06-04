package rapizz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import rapizz.model.Client;
import rapizz.model.Livreur;
import rapizz.model.Pizza;
import rapizz.model.Vehicule;
import rapizz.util.DBConnection;

public class JDBCCommandeDAO implements CommandeDAO {

    @Override
    public List<Client> getClients() throws Exception {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT id_client, nom, prenom, numero_de_telephone, solde " +
                     "FROM Client ORDER BY nom, prenom";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Client(
                    rs.getInt("id_client"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("numero_de_telephone"),
                    rs.getDouble("solde")
                ));
            }
        }
        return list;
    }

    @Override
    public List<Pizza> getPizzas() throws Exception {
        List<Pizza> list = new ArrayList<>();
        String sql = "SELECT id_pizza, nom, prix_de_base FROM Pizza ORDER BY nom";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Pizza(
                    rs.getInt("id_pizza"),
                    rs.getString("nom"),
                    rs.getDouble("prix_de_base")
                ));
            }
        }
        return list;
    }

    @Override
    public List<Livreur> getLivreurs() throws Exception {
        List<Livreur> list = new ArrayList<>();
        String sql = "SELECT id_livreur, nom, prenom FROM Livreur ORDER BY nom, prenom";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Livreur(
                    rs.getInt("id_livreur"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                ));
            }
        }
        return list;
    }

    @Override
    public List<Vehicule> getVehicules() throws Exception {
        List<Vehicule> list = new ArrayList<>();
        String sql = "SELECT id_vehicule, nom, type_vehicule FROM Vehicule ORDER BY nom";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Vehicule(
                    rs.getInt("id_vehicule"),
                    rs.getString("nom"),
                    rs.getString("type_vehicule")
                ));
            }
        }
        return list;
    }

    @Override
    public void createCommande(int idClient, int idPizza, int idLivreur, int idVehicule,
                               double taille) throws Exception {
        String priceSql = "SELECT prix_de_base FROM Pizza WHERE id_pizza = ?";
        String soldeSql = "SELECT solde FROM Client WHERE id_client = ?";
        String nextIdSql = "SELECT COALESCE(MAX(idlivraison), 0) + 1 as next_id FROM Livraison";
        String insertSql = "INSERT INTO Livraison (idlivraison, date, temps, prix_facture, est_gratuite, taille, " +
                           "id_vehicule, id_livreur, id_pizza, id_client) " +
                           "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";
        String updateSoldeSql = "UPDATE Client SET solde = solde - ? WHERE id_client = ?";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                double basePrice = 0;
                try (PreparedStatement ps = c.prepareStatement(priceSql)) {
                    ps.setInt(1, idPizza);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            basePrice = rs.getDouble("prix_de_base");
                        } else {
                            throw new IllegalStateException("Pizza introuvable.");
                        }
                    }
                }

                double prixFacture = basePrice * taille;
                double solde = 0;
                try (PreparedStatement ps = c.prepareStatement(soldeSql)) {
                    ps.setInt(1, idClient);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            solde = rs.getDouble("solde");
                        } else {
                            throw new IllegalStateException("Client introuvable.");
                        }
                    }
                }

                if (solde < prixFacture) {
                    throw new IllegalStateException("Solde insuffisant pour honorer la commande.");
                }

                int nextId = 0;
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery(nextIdSql)) {
                    if (rs.next()) {
                        nextId = rs.getInt("next_id");
                    }
                }

                int temps = ThreadLocalRandom.current().nextInt(5, 36);
                try (PreparedStatement ps = c.prepareStatement(insertSql)) {
                    ps.setInt(1, nextId);
                    ps.setInt(2, temps);
                    ps.setDouble(3, prixFacture);
                    ps.setBoolean(4, false);
                    ps.setDouble(5, taille);
                    ps.setInt(6, idVehicule);
                    ps.setInt(7, idLivreur);
                    ps.setInt(8, idPizza);
                    ps.setInt(9, idClient);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = c.prepareStatement(updateSoldeSql)) {
                    ps.setDouble(1, prixFacture);
                    ps.setInt(2, idClient);
                    ps.executeUpdate();
                }

                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
}
