package rapizz.dao;

import java.sql.*;
import java.util.*;
import rapizz.util.DBConnection;

public class StatisticsDAO {
    
    public List<String[]> getUnusedVehicles() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT v.nom FROM Vehicule v " +
                     "WHERE v.id_vehicule NOT IN (SELECT DISTINCT id_vehicule FROM Livraison)";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new String[] { rs.getString("nom") });
            }
        }
        return list;
    }
    
    public List<String[]> getOrdersPerClient() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT c.nom, c.prenom, COUNT(*) as count " +
                     "FROM Client c, Livraison liv " +
                     "WHERE c.id_client = liv.id_client " +
                     "GROUP BY c.id_client ORDER BY c.nom";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new String[] { rs.getString("nom"), rs.getString("prenom"), rs.getString("count") });
            }
        }
        return list;
    }
    
    public double getAverageOrders() throws Exception {
        String sql = "SELECT AVG(nb_pizzas) as avg_pizzas FROM (" +
                     "SELECT COUNT(*) as nb_pizzas FROM Livraison GROUP BY id_client" +
                     ") as commandes";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("avg_pizzas");
            }
        }
        return 0;
    }

    public double getTotalRevenue() throws Exception {
        String sql = "SELECT COALESCE(SUM(prix_facture), 0) as ca FROM Livraison";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("ca");
            }
        }
        return 0;
    }
    
    public List<String[]> getClientsAboveAverage() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT c.nom, c.prenom, COUNT(*) as count " +
                     "FROM Client c, Livraison liv " +
                     "WHERE c.id_client = liv.id_client " +
                     "GROUP BY c.id_client " +
                     "HAVING COUNT(*) > (" +
                     "SELECT AVG(nb_pizzas) FROM (" +
                     "SELECT COUNT(*) as nb_pizzas FROM Livraison GROUP BY id_client" +
                     ") as avg_pizzas) " +
                     "ORDER BY count DESC";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new String[] { rs.getString("nom"), rs.getString("prenom"), rs.getString("count") });
            }
        }
        return list;
    }

    public List<String[]> getBestClient() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT c.nom, c.prenom, COUNT(*) as count " +
                     "FROM Client c JOIN Livraison liv ON c.id_client = liv.id_client " +
                     "GROUP BY c.id_client " +
                     "ORDER BY count DESC, c.nom, c.prenom " +
                     "LIMIT 1";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[] { rs.getString("nom"), rs.getString("prenom"), rs.getString("count") });
            }
        }
        return list;
    }

    public List<String[]> getWorstDeliverer() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT l.nom, l.prenom, v.nom as vehicule, COUNT(*) as retards " +
                     "FROM Livraison liv " +
                     "JOIN Livreur l ON liv.id_livreur = l.id_livreur " +
                     "JOIN Vehicule v ON liv.id_vehicule = v.id_vehicule " +
                     "WHERE liv.temps > 30 " +
                     "GROUP BY l.id_livreur, v.id_vehicule " +
                     "ORDER BY retards DESC, l.nom, l.prenom " +
                     "LIMIT 1";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String livreur = rs.getString("nom") + " " + rs.getString("prenom");
                list.add(new String[] { livreur, rs.getString("vehicule"), rs.getString("retards") });
            }
        }
        return list;
    }

    public List<String[]> getMostOrderedPizza() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT p.nom, COUNT(liv.id_pizza) as count " +
                     "FROM Pizza p LEFT JOIN Livraison liv ON p.id_pizza = liv.id_pizza " +
                     "GROUP BY p.id_pizza " +
                     "ORDER BY count DESC, p.nom " +
                     "LIMIT 1";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[] { rs.getString("nom"), rs.getString("count") });
            }
        }
        return list;
    }

    public List<String[]> getLeastOrderedPizza() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT p.nom, COUNT(liv.id_pizza) as count " +
                     "FROM Pizza p LEFT JOIN Livraison liv ON p.id_pizza = liv.id_pizza " +
                     "GROUP BY p.id_pizza " +
                     "ORDER BY count ASC, p.nom " +
                     "LIMIT 1";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[] { rs.getString("nom"), rs.getString("count") });
            }
        }
        return list;
    }

    public List<String[]> getFavoriteIngredient() throws Exception {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT i.nom, COALESCE(SUM(ud.quantite), 0) as total " +
                     "FROM Livraison liv " +
                     "JOIN utilise_dans ud ON liv.id_pizza = ud.id_pizza " +
                     "JOIN Ingredient i ON ud.id_ing = i.id_ing " +
                     "GROUP BY i.id_ing " +
                     "ORDER BY total DESC, i.nom " +
                     "LIMIT 1";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[] { rs.getString("nom"), rs.getString("total") });
            }
        }
        return list;
    }
}
