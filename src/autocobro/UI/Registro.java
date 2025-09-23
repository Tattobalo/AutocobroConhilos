package autocobro.UI;

import autocobro.Nucleo.RegistroThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class Registro extends JPanel {

    private FrameP framePrincipal;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JTextField campoCorreo;
    private String rutaFoto; // <-- se mantiene

    public Registro(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(null); // 🔹 coordenadas absolutas
        setBackground(new Color(220, 220, 220));

        JPanel panelPrincipal = crearPanel();
        panelPrincipal.setBounds(100, 50, 600, 500);
        add(panelPrincipal);
    }

    private JPanel crearPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(0, 204, 153);
                Color color2 = new Color(15, 60, 15);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                int radioEsquina = 30;
                RoundRectangle2D rectanguloRedondeado = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radioEsquina, radioEsquina);
                g2d.fill(rectanguloRedondeado);
            }
        };
        
        panel.setLayout(null);
        panel.setOpaque(false);
        
        // 🔹 Título
        JLabel titulo = new JLabel("Registro");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(100, 40, 200, 40);
        panel.add(titulo);

        // 🔹 Usuario
        JLabel etiquetaUsuario = new JLabel("Nombre de Usuario *");
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaUsuario.setForeground(Color.WHITE);
        etiquetaUsuario.setBounds(50, 100, 200, 20);
        panel.add(etiquetaUsuario);

        campoUsuario = new CampoTextoRedondeado(20);
        campoUsuario.setBounds(50, 125, 250, 30);
        panel.add(campoUsuario);

        // 🔹 Contraseña
        JLabel etiquetaContrasena = new JLabel("Contraseña *");
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContrasena.setForeground(Color.WHITE);
        etiquetaContrasena.setBounds(50, 170, 200, 20);
        panel.add(etiquetaContrasena);

        campoContrasena = new CampoContrasenaRedondeado(20);
        campoContrasena.setBounds(50, 195, 250, 30);
        panel.add(campoContrasena);

        // 🔹 Teléfono
        JLabel etiquetaTelefono = new JLabel("Correo *");
        etiquetaTelefono.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaTelefono.setForeground(Color.WHITE);
        etiquetaTelefono.setBounds(50, 240, 200, 20);
        panel.add(etiquetaTelefono);

        campoCorreo = new CampoTextoRedondeado(20);
        campoCorreo.setBounds(50, 265, 250, 30);
        panel.add(campoCorreo);

        // 🔹 Foto de perfil
        JLabel etiquetaFoto = new JLabel("Foto de perfil (Opcional)");
        etiquetaFoto.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaFoto.setForeground(Color.WHITE);
        etiquetaFoto.setBounds(350, 100, 200, 20);
        panel.add(etiquetaFoto);

        CampoFoto campoFoto = new CampoFoto(150, 150, true);
        campoFoto.setBounds(360, 125, 150, 150);
        campoFoto.setOpaque(false);
        panel.add(campoFoto);

        // 🔹 Botón registrar
        JButton botonRegistrarse = new BotonRedondeado("Registrarse", new Color(153, 51, 255));
        botonRegistrarse.setBounds(180, 340, 200, 40);
        panel.add(botonRegistrarse);
        
        
        botonRegistrarse.addActionListener(e -> {
            String usuario = campoUsuario.getText();
            String contrasena = new String(campoContrasena.getPassword());
            String correo = campoCorreo.getText();

            if (usuario.isEmpty() || contrasena.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Inicia el hilo de registro
            new RegistroThread(usuario, contrasena, correo, this.rutaFoto, this, framePrincipal).start();
        });
        
        return panel;
    }
}
