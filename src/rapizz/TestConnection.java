package rapizz;

import java.sql.*;
import rapizz.util.DBConnection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // 1. Vérifier que le driver est chargé
            System.out.println("=== DIAGNOSTIC DRIVER ===");
            try {
                Driver driver = DriverManager.getDriver("jdbc:mysql://127.0.0.1:3306/");
                System.out.println("✓ Driver MySQL trouvé: " + driver.getClass().getName());
                System.out.println("  Version: " + driver.getMajorVersion() + "." + driver.getMinorVersion());
            } catch (SQLException e) {
                System.out.println("✗ Driver MySQL non trouvé dans DriverManager");
            }
            
            // 2. Tenter connexion
            System.out.println("\n=== TENTATIVE DE CONNEXION ===");
            System.out.println("Tentative de connexion à MySQL...");
            Connection conn = DBConnection.getConnection();
            System.out.println("✓ Connexion réussie!");
            
            // 3. Vérifier la DB
            System.out.println("\n=== TESTS REQUÊTE ===");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) as cnt FROM Pizza");
            if (rs.next()) {
                System.out.println("✓ Nombre de pizzas: " + rs.getInt("cnt"));
            }
            
            rs.close();
            st.close();
            conn.close();
            System.out.println("\n✓ Tous les tests passés!");
            
        } catch (Exception e) {
            System.err.println("\n✗ Erreur:");
            e.printStackTrace();
        }
    }
}
