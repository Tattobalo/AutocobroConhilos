package autocobro.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectorBD {
    
    private static final String URL = "jdbc:mysql://localhost:3307/autocobro?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Guri08Khyes";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
    
    public static void main(String[] args) {
        try (Connection conexion = ConectorBD.conectar()) {
            System.out.println("¡Conexión exitosa a la base de datos!");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}