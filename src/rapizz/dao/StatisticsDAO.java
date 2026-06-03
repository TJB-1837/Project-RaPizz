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
}
