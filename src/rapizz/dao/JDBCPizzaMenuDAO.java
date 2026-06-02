package rapizz.dao;

import java.sql.*;
import java.util.*;
import rapizz.model.*;
import rapizz.util.DBConnection;

public class JDBCPizzaMenuDAO implements PizzaMenuDAO {
    
    @Override
    public List<PizzaMenu> findMenu() throws Exception {
        List<PizzaMenu> list = new ArrayList<>();
        String sql = "SELECT p.id_pizza, p.nom, p.prix_de_base, i.nom, ud.quantite " +
                     "FROM Pizza p " +
                     "LEFT JOIN utilise_dans ud ON p.id_pizza = ud.id_pizza " +
                     "LEFT JOIN Ingredient i ON ud.id_ing = i.id_ing " +
                     "ORDER BY p.id_pizza, i.nom";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new PizzaMenu(
                    rs.getInt("id_pizza"),
                    rs.getString("nom"),
                    rs.getDouble("prix_de_base"),
                    rs.getString("i.nom"),
                    rs.getInt("quantite")
                ));
            }
        }
        return list;
    }
}
