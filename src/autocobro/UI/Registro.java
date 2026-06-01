package autocobro.UI;

import autocobro.Nucleo.RegistroThread;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class Registro extends JPanel {

    private FrameP framePrincipal;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JTextField campoCorreo;
    private CampoFoto campoFotoComponente; // Atributo global que retiene el estado de la foto

    public Registro(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(null); // Coordenadas absolutas externas
        setBackground(new Color(220, 220, 220));

        // Contenedor principal centrado
        JPanel panelPrincipal = crearPanel();
        panelPrincipal.setBounds(100, 50, 600, 480); 
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

        // Título de la tarjeta
        JLabel titulo = new JLabel("Registro");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(50, 35, 200, 40);
        panel.add(titulo);

        // ================= COLUMNA IZQUIERDA: DATOS DE ACCESO =================
        // Usuario
        JLabel etiquetaUsuario = new JLabel("Nombre de Usuario *");
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaUsuario.setForeground(Color.WHITE);
        etiquetaUsuario.setBounds(50, 95, 200, 20);
        panel.add(etiquetaUsuario);

        campoUsuario = new CampoTextoRedondeado(20);
        campoUsuario.setBounds(50, 120, 250, 30);
        panel.add(campoUsuario);

        // Contraseña
        JLabel etiquetaContrasena = new JLabel("Contraseña *");
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContrasena.setForeground(Color.WHITE);
        etiquetaContrasena.setBounds(50, 165, 200, 20);
        panel.add(etiquetaContrasena);

        campoContrasena = new CampoContrasenaRedondeado(20);
        campoContrasena.setBounds(50, 190, 250, 30);
        panel.add(campoContrasena);

        // Correo
        JLabel etiquetaTelefono = new JLabel("Correo *");
        etiquetaTelefono.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaTelefono.setForeground(Color.WHITE);
        etiquetaTelefono.setBounds(50, 235, 200, 20);
        panel.add(etiquetaTelefono);

        campoCorreo = new CampoTextoRedondeado(20);
        campoCorreo.setBounds(50, 260, 250, 30);
        panel.add(campoCorreo);

        // ================= COLUMNA DERECHA: FOTO DE PERFIL =================
        JLabel etiquetaFoto = new JLabel("Foto de perfil (Opcional)");
        etiquetaFoto.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaFoto.setForeground(Color.WHITE);
        etiquetaFoto.setBounds(350, 95, 200, 20);
        panel.add(etiquetaFoto);

        campoFotoComponente = new CampoFoto(150, 150, true);
        campoFotoComponente.setBounds(360, 120, 150, 150);
        campoFotoComponente.setOpaque(false);
        panel.add(campoFotoComponente);

        // ================= PARTE INFERIOR: BOTONES REDISEÑADOS ESTÉTICAMENTE =================
        // Se amplió el ancho a 180 para que el texto quepa completo sin recortarse
        
        // 🔹 BOTÓN 1: REGISTRARSE (Blanco minimalista con tipografía verde oscura elegante)
        JButton botonRegistrarse = new BotonRedondeado("Registrarse", Color.WHITE);
        botonRegistrarse.setBounds(110, 360, 180, 40); 
        botonRegistrarse.setForeground(new Color(15, 60, 15)); 
        botonRegistrarse.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(botonRegistrarse);

        // 🔹 BOTÓN 2: REGRESAR AL LOGIN (Verde menta pastel suave integrado con el fondo)
        JButton botonRegresar = new BotonRedondeado("Regresar", new Color(175, 240, 215)); 
        botonRegresar.setBounds(310, 360, 180, 40); 
        botonRegresar.setForeground(new Color(10, 50, 30));
        botonRegresar.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(botonRegresar);


        // Evento de acción: Registro
        botonRegistrarse.addActionListener(e -> {
            String usuario = campoUsuario.getText();
            String contrasena = new String(campoContrasena.getPassword());
            String correo = campoCorreo.getText();

            String rutaFotoTemporal = campoFotoComponente.getRutaArchivoSeleccionado();
            if (rutaFotoTemporal != null && !rutaFotoTemporal.isEmpty()) {
                File archivo = new File(rutaFotoTemporal);
                rutaFotoTemporal = archivo.getAbsolutePath(); 
            }

            new RegistroThread(usuario, contrasena, correo, rutaFotoTemporal, this, framePrincipal).start();
        });

        // Evento de acción: Regresar
        botonRegresar.addActionListener(e -> {
            campoUsuario.setText("");
            campoContrasena.setText("");
            campoCorreo.setText("");
            if (campoFotoComponente != null) {
                campoFotoComponente.restablecerImagen();
            }
            framePrincipal.mostrarPanel(FrameP.LOGIN_PANEL);
        });

        return panel;
    }

    // MÉTODOS GETTERS PÚBLICOS
    public JTextField getCampoUsuario() {
        return campoUsuario;
    }

    public JPasswordField getCampoContrasena() {
        return campoContrasena;
    }

    public JTextField getCampoCorreo() {
        return campoCorreo;
    }

    public CampoFoto getCampoFotoComponente() {
        return campoFotoComponente;
    }
}