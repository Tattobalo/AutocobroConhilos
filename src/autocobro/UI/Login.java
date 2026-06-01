package autocobro.UI;

import autocobro.Nucleo.LoginThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class Login extends JPanel {

    private final FrameP framePrincipal;
    
    // Atributos de clase globales para que los getters tengan acceso a la memoria
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;

    public Login(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(null); // 🔹 Coordenadas absolutas
        setBackground(new Color(220, 220, 220));

        JPanel panelPrincipal = crearPanel();
        panelPrincipal.setBounds(200, 50, 400, 500);
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
        
        // Título
        JLabel titulo = new JLabel("AutoCobro");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(100, 40, 300, 40);
        panel.add(titulo);

        // Usuario
        JLabel etiquetaUsuario = new JLabel("Usuario");
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaUsuario.setForeground(Color.WHITE);
        etiquetaUsuario.setBounds(50, 120, 100, 20);
        panel.add(etiquetaUsuario);

        // CORRECCIÓN: Quitamos 'JTextField' de adelante para usar el atributo global de la clase
        campoUsuario = new CampoTextoRedondeado(20);
        campoUsuario.setBounds(50, 150, 300, 30);
        panel.add(campoUsuario);

        // Contraseña
        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaContrasena.setForeground(Color.WHITE);
        etiquetaContrasena.setBounds(50, 200, 100, 20);
        panel.add(etiquetaContrasena);

        // CORRECCIÓN: Quitamos 'JPasswordField' de adelante para usar el atributo global de la clase
        campoContrasena = new CampoContrasenaRedondeado(20);
        campoContrasena.setBounds(50, 230, 300, 30);
        panel.add(campoContrasena);

        // Botón Ingresar
        JButton botonIngresar = new BotonRedondeado("Inicia sesión", new Color(153, 51, 255));
        botonIngresar.setBounds(100, 280, 200, 40);
        panel.add(botonIngresar);

        botonIngresar.addActionListener(e -> {
            String usuario = campoUsuario.getText();
            String contrasena = new String(campoContrasena.getPassword());
            LoginThread loginThread = new LoginThread(usuario, contrasena, this, framePrincipal);
            loginThread.start();
        });

        // Registro
        JLabel etiquetaNoCuenta = new JLabel("¿No tienes una cuenta?");
        etiquetaNoCuenta.setFont(new Font("Arial", Font.PLAIN, 12));
        etiquetaNoCuenta.setForeground(Color.WHITE);
        etiquetaNoCuenta.setBounds(150, 350, 150, 20);
        panel.add(etiquetaNoCuenta);

        JLabel enlaceRegistrarse = new JLabel("<html><u>Regístrate</u></html>");
        enlaceRegistrarse.setFont(new Font("Arial", Font.BOLD, 12));
        enlaceRegistrarse.setForeground(new Color(153, 51, 255));
        enlaceRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        enlaceRegistrarse.setBounds(300, 350, 100, 20);
        enlaceRegistrarse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                framePrincipal.mostrarPanel(FrameP.REGISTRO_PANEL);
            }
        });
        panel.add(enlaceRegistrarse);

        return panel;
    }

    // Métodos Getter públicos que solucionan los errores de compilación en LoginThread
    public JTextField getCampoUsuario() {
        return this.campoUsuario;
    }

    public JPasswordField getCampoContrasena() {
        return this.campoContrasena;
    }
}