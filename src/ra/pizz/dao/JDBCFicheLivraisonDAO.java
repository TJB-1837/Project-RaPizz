package ra.pizz.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import ra.pizz.model.*;
import ra.pizz.util.DBConnection;

public class JDBCFicheLivraisonDAO implements FicheLivraisonDAO {
    
    @Override
    public List<FicheLivraison> findAll() throws Exception {
        List<FicheLivraison> list = new ArrayList<>();
        String sql = "SELECT l.nom, l.prenom, v.type_vehicule, c.nom as nom_client, c.prenom as prenom_client, " +
                     "liv.date, liv.temps, p.nom as nom_pizza, liv.prix_facture, " +
                     "CASE WHEN liv.temps > 30 THEN liv.temps - 30 ELSE 0 END as retard " +
                     "FROM Livreur l, Vehicule v, Client c, Livraison liv, Pizza p " +
                     "WHERE liv.id_livreur = l.id_livreur AND liv.id_vehicule = v.id_vehicule " +
                     "AND liv.id_client = c.id_client AND liv.id_pizza = p.id_pizza " +
                     "ORDER BY liv.date";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("date");
                LocalDateTime date = ts != null ? ts.toLocalDateTime() : null;
                
                list.add(new FicheLivraison(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("type_vehicule"),
                    rs.getString("nom_client"),
                    rs.getString("prenom_client"),
                    rs.getString("nom_pizza"),
                    date,
                    rs.getInt("retard"),
                    rs.getDouble("prix_facture")
                ));
            }
        }
        return list;
    }
}
