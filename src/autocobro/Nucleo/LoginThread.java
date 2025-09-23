package autocobro.Nucleo;

import autocobro.UI.FrameP;
import autocobro.UI.Login;
import autocobro.Modelos.Usuarios;
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
    
    private String nombreUsuario;
    private String contrasena;
    private Login loginPanel;
    private FrameP framePrincipal;
    
    public LoginThread(String nombreUsuario, String contrasena, Login loginPanel, FrameP framePrincipal) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.loginPanel = loginPanel;
        this.framePrincipal = framePrincipal;
    }

    @Override
    public void run() {
        System.out.println("HILO 1 (Login): Iniciando proceso de autenticación para " + nombreUsuario + ".");

        Connection conexion = null;
        try {
            conexion = ConectorBD.conectar();
            String query = "SELECT id, nombre_usuario, correo, ruta_foto_perfil FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            
            String contrasenaHash = hashearContrasena(contrasena);
            
            try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
                pstmt.setString(1, nombreUsuario);
                pstmt.setString(2, contrasenaHash);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String nombreUsuario = rs.getString("nombre_usuario");
                        String correo = rs.getString("correo");
                        String rutaFoto = rs.getString("ruta_foto_perfil");
                        
                        Usuarios usuario = new Usuarios(id, nombreUsuario, correo, rutaFoto);
                        
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(loginPanel, "¡Inicio de sesión exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            
                            framePrincipal.setUsuarioActual(usuario);
                            framePrincipal.iniciarSesion(); // <-- Inicia el HILO 2 (Sesión) aquí
                            
                            framePrincipal.mostrarPanel(FrameP.PRODUCTOS_PANEL); 
                        });
                        System.out.println("HILO 1 (Login): Autenticación exitosa. Se inició la sesión.");
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(loginPanel, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                        });
                        System.out.println("HILO 1 (Login): Autenticación fallida. Usuario o contraseña incorrectos.");
                    }
                }
            }
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(loginPanel, "Error al conectar a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
            System.out.println("HILO 1 (Login): Error de SQL: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(loginPanel, "Error de seguridad al verificar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            System.out.println("HILO 1 (Login): Error de seguridad al hashear la contraseña.");
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    // Ignorar
                }
            }
        }
        System.out.println("HILO 1 (Login): Hilo finalizado.");
    }

    private String hashearContrasena(String contrasenaPlana) throws NoSuchAlgorithmException {
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
        return hexString.toString();
    }
    
    private boolean verificarContrasena(String contrasenaPlana, String contrasenaHash) throws NoSuchAlgorithmException {
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
    }
}