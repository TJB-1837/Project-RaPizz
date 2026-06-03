package rapizz.dao;

import java.sql.*;
import java.util.*;
import rapizz.model.*;
import rapizz.util.DBConnection;

public class JDBCPizzaMenuDAO implements PizzaMenuDAO {
    
    @Override
    public List<PizzaMenu> findMenu() throws Exception {
        List<PizzaMenu> list = new ArrayList<>();
        String sql = "SELECT p.id_pizza, p.nom, p.prix_de_base, " +
                 "GROUP_CONCAT(i.nom ORDER BY i.nom SEPARATOR '|') AS ingredients, " +
                 "GROUP_CONCAT(COALESCE(ud.quantite, 0) ORDER BY i.nom SEPARATOR '|') AS quantites " +
                     "FROM Pizza p " +
                     "LEFT JOIN utilise_dans ud ON p.id_pizza = ud.id_pizza " +
                     "LEFT JOIN Ingredient i ON ud.id_ing = i.id_ing " +
                     "GROUP BY p.id_pizza, p.nom, p.prix_de_base " +
                     "ORDER BY p.id_pizza";
        
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                String ingredientsRaw = rs.getString("ingredients");
                String quantitesRaw = rs.getString("quantites");
                Map<String, Integer> ingredients = new HashMap<>();
                if (ingredientsRaw != null && !ingredientsRaw.isBlank()) {
                    String[] names = ingredientsRaw.split("\\|");
                    String[] qtyParts = new String[0];
                    if (quantitesRaw != null && !quantitesRaw.isBlank()) {
                        qtyParts = quantitesRaw.split("\\|");
                    }
                    for (int i = 0; i < names.length; i++) {
                        int qty = 0;
                        if (i < qtyParts.length) {
                            try {
                                qty = Integer.parseInt(qtyParts[i]);
                            } catch (NumberFormatException ignored) {
                                qty = 0;
                            }
                        }
                        ingredients.put(names[i], qty);
                    }
                }

                list.add(new PizzaMenu(
                    rs.getInt("id_pizza"),
                    rs.getString("nom"),
                    rs.getDouble("prix_de_base"),
                    ingredients
                ));
            }
        }
        return list;
    }
}
