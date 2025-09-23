package autocobro.Nucleo;

import autocobro.UI.FrameP;
import autocobro.UI.Registro;
import autocobro.Util.ConectorBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RegistroThread extends Thread {
    
    private String nombreUsuario;
    private String contrasena;
    private String correo;
    private String rutaFoto; // La ruta temporal de la foto seleccionada
    private Registro registroPanel;
    private FrameP framePrincipal;
    
    public RegistroThread(String nombreUsuario, String contrasena, String correo, String rutaFoto, Registro registroPanel, FrameP framePrincipal) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.rutaFoto = rutaFoto;
        this.registroPanel = registroPanel;
        this.framePrincipal = framePrincipal;
    }

    @Override
    public void run() {
        Connection conexion = null;
        try {
            conexion = ConectorBD.conectar();
            String contrasenaHash = hashearContrasena(contrasena);
            
            String nombreArchivoFoto = null;
            if (rutaFoto != null && !rutaFoto.isEmpty()) {
                try {
                    File archivoOriginal = new File(rutaFoto);
                    String nombreDeArchivo = archivoOriginal.getName();
                    
                    Path destino = Paths.get("fotos_perfil", nombreDeArchivo);
                    Files.createDirectories(destino.getParent());
                    Files.copy(archivoOriginal.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                    // Solo guarda el nombre del archivo en la base de datos
                    nombreArchivoFoto = destino.getFileName().toString(); 
                } catch (Exception e) {
                     SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(registroPanel, "Error al guardar la foto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                    return; // Detiene el hilo si no se puede guardar la foto
                }
            }
            
            String query = "INSERT INTO usuarios (nombre_usuario, contrasena, correo, ruta_foto_perfil) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
                pstmt.setString(1, nombreUsuario);
                pstmt.setString(2, contrasenaHash);
                pstmt.setString(3, correo);
                pstmt.setString(4, nombreArchivoFoto);
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(registroPanel, "¡Registro exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        framePrincipal.mostrarPanel(FrameP.LOGIN_PANEL);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(registroPanel, "No se pudo registrar al usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(registroPanel, "Error al conectar a la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        } catch (NoSuchAlgorithmException e) {
             SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(registroPanel, "Error de seguridad al hashear la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
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
}