package rapizz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Minimal student setup: use local root with no password on 127.0.0.1
    // Change USER/PASSWORD here if your root has a password.
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/RaPizz?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "dev";
    private static final String PASSWORD = "";

    static {
        try {
            // Load driver per MySQL official docs: https://dev.mysql.com/doc/connector-j/8.0/en/
            // The newInstance() call is a workaround for some broken Java implementations
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Erreur: Driver MySQL non trouvé ou non initialisé: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
