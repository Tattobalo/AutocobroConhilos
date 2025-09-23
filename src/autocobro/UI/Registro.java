package autocobro.UI;

import autocobro.Nucleo.RegistroThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Registro extends JPanel {

    private FrameP framePrincipal;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JTextField campoTelefono;
    private String rutaFoto; // <-- ¡Agrega esta línea!

    public Registro(FrameP framePrincipal) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(400, 600));
        setBackground(new Color(220, 220, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Registro");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(60, 20, 40, 20);
        add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel etiquetaUsuario = new JLabel("Nombre de Usuario *");
        etiquetaUsuario.setOpaque(false);
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaUsuario.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 5, 20);
        add(etiquetaUsuario, gbc);

        JTextField campoUsuario = new CampoTextoRedondeado(20);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 15, 20);
        add(campoUsuario, gbc);

        JLabel etiquetaContrasena = new JLabel("Contraseña *");
        etiquetaContrasena.setOpaque(false);
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContrasena.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 5, 20);
        add(etiquetaContrasena, gbc);

        JPasswordField campoContrasena = new CampoContrasenaRedondeado(20);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 20, 15, 20);
        add(campoContrasena, gbc);

        JLabel etiquetaTelefono = new JLabel("Numero de telefono *");
        etiquetaTelefono.setOpaque(false);
        etiquetaTelefono.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaTelefono.setForeground(Color.WHITE);
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 5, 20);
        add(etiquetaTelefono, gbc);

        JTextField campoTelefono = new CampoTextoRedondeado(20);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 20, 30, 20);
        add(campoTelefono, gbc);

        JLabel etiquetaFoto = new JLabel("Foto de perfil (Opcional)");
        etiquetaFoto.setOpaque(false);
        etiquetaFoto.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaFoto.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 20, 5, 20);
        add(etiquetaFoto, gbc);

        CampoFoto campoFoto = new CampoFoto(150, 150);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 20, 15, 20);
        gbc.gridheight = 4;
        add(campoFoto, gbc);

        JButton botonRegistrarse = new BotonRedondeado("Registrarse", new Color(153, 51, 255));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 20, 20, 20);
        add(botonRegistrarse, gbc);

        // Lógica del HILO 1: Registro
        botonRegistrarse.addActionListener(e -> {
            String usuario = campoUsuario.getText();
            String contrasena = new String(campoContrasena.getPassword());
            String telefono = campoTelefono.getText();

            if (usuario.isEmpty() || contrasena.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Inicia el hilo de registro
            new RegistroThread(usuario, contrasena, telefono, this.rutaFoto, this, framePrincipal).start();
        });

    }

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
}
