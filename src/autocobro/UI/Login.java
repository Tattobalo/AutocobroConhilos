package autocobro.UI;

import autocobro.Nucleo.LoginThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class Login extends JPanel {

    public Login(FrameP framePrincipal) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(400, 600));
        setBackground(new Color(220, 220, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("AutoCobro");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(60, 20, 40, 20);
        add(titulo, gbc);

        gbc.gridwidth = 1;
        JLabel etiquetaUsuario = new JLabel("Usuario");
        etiquetaUsuario.setOpaque(false);
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaUsuario.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 5, 20);
        add(etiquetaUsuario, gbc);

        JTextField campoUsuario = new CampoTextoRedondeado(20);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 15, 20);
        add(campoUsuario, gbc);

        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setOpaque(false);
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContrasena.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 5, 20);
        add(etiquetaContrasena, gbc);

        JPasswordField campoContrasena = new CampoContrasenaRedondeado(20);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 20, 30, 20);
        add(campoContrasena, gbc);

        JButton botonIngresar = new BotonRedondeado("Inicia sesión", new Color(153, 51, 255));
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 20, 20);
        add(botonIngresar, gbc);

        botonIngresar.addActionListener(e -> {
            // Obtiene la información de los campos de texto
            String usuario = campoUsuario.getText();
            String contrasena = new String(campoContrasena.getPassword());

            // Crea una instancia del hilo de login, pasándole los datos y las referencias necesarias
            LoginThread loginThread = new LoginThread(usuario, contrasena, this, framePrincipal);
            loginThread.start();
        });

        JPanel panelRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelRegistro.setOpaque(false);
        JLabel etiquetaNoCuenta = new JLabel("¿No tienes una cuenta?");
        etiquetaNoCuenta.setOpaque(false);
        etiquetaNoCuenta.setFont(new Font("Arial", Font.PLAIN, 12));
        etiquetaNoCuenta.setForeground(Color.WHITE);

        JLabel enlaceRegistrarse = new JLabel("<html><u>Regístrate</u></html>");
        enlaceRegistrarse.setOpaque(false);
        enlaceRegistrarse.setFont(new Font("Arial", Font.BOLD, 12));
        enlaceRegistrarse.setForeground(new Color(153, 51, 255));

        enlaceRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        enlaceRegistrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                framePrincipal.mostrarPanel(FrameP.REGISTRO_PANEL);
            }
        });

        panelRegistro.add(etiquetaNoCuenta);
        panelRegistro.add(enlaceRegistrarse);

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 20, 40, 20);
        add(panelRegistro, gbc);
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
