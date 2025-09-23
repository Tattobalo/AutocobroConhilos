package autocobro.Nucleo;

import autocobro.UI.FrameP;
import autocobro.UI.Login;
import autocobro.UI.Productos;
import autocobro.Util.ConectorBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginThread extends Thread {
    
    private final String nombreUsuario;
    private final String contrasena;
    private final Login loginPanel;
    private final FrameP framePrincipal;
    
    public LoginThread(String nombreUsuario, String contrasena, Login loginPanel, FrameP framePrincipal) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.loginPanel = loginPanel;
        this.framePrincipal = framePrincipal;
    }

    @Override
    public void run() {
        Connection conexion = null;
        try {
            conexion = ConectorBD.conectar();
            String query = "SELECT contrasena FROM usuarios WHERE nombre_usuario = ?";
            
            // Usamos PreparedStatement para evitar inyecciones SQL
            try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
                pstmt.setString(1, nombreUsuario);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String contrasenaHashGuardada = rs.getString("contrasena");
                        
                        // Verifica la contraseña
                        if (verificarContrasena(contrasena, contrasenaHashGuardada)) {
                            // Si el login es exitoso, actualiza la UI en el EDT
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(loginPanel, "¡Inicio de sesión exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                // Aquí puedes mostrar la siguiente interfaz (ej. 'Productos')
                                framePrincipal.mostrarPanel(FrameP.PRODUCTOS_PANEL); 
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(loginPanel, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                            });
                        }
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(loginPanel, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }
            }
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(loginPanel, "Error al conectar a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    // Ignorar
                }
            }
        }
    }

    // Método para hashear y verificar contraseñas de forma segura
    private boolean verificarContrasena(String contrasenaPlana, String contrasenaHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contrasenaPlana.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().equals(contrasenaHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}