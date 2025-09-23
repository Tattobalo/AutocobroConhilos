package autocobro.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import autocobro.Modelos.Usuarios;
import autocobro.Nucleo.SesionThread;

public class FrameP extends JFrame {

    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String REGISTRO_PANEL = "RegistroPanel";
    public static final String PRODUCTOS_PANEL = "ProductosPanel";
    public static final String MIS_PRODUCTOS_PANEL = "MisProductosPanel"; // <-- Nueva constante

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    
    private SesionThread sesionThread;
    private Usuarios usuarioActual;

    public FrameP() {
        setTitle("AutoCobro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(new Color(220, 220, 220));

        JPanel titleBarPanel = new JPanel();
        titleBarPanel.setLayout(new BoxLayout(titleBarPanel, BoxLayout.X_AXIS));
        titleBarPanel.setBackground(new Color(220, 220, 220));

        titleBarPanel.add(Box.createHorizontalGlue());

        JButton minimizeButton = createControlButton("—");
        minimizeButton.addActionListener(e -> setExtendedState(JFrame.ICONIFIED));
        titleBarPanel.add(minimizeButton);

        JButton closeButton = createControlButton("X");
        closeButton.addActionListener(e -> System.exit(0));
        titleBarPanel.add(closeButton);

        mainContainer.add(titleBarPanel, BorderLayout.NORTH);
        
        // Instancia y agrega los paneles al CardLayout
        Login loginPanel = new Login(this);
        Registro registroPanel = new Registro(this);
        Productos productosPanel = new Productos(this);
        MisProductos misProductosPanel = new MisProductos(this); // <-- Nueva instancia
        
        cardPanel.add(loginPanel, LOGIN_PANEL);
        cardPanel.add(registroPanel, REGISTRO_PANEL);
        cardPanel.add(productosPanel, PRODUCTOS_PANEL);
        cardPanel.add(misProductosPanel, MIS_PRODUCTOS_PANEL); // <-- Agrega el panel de Mis Productos

        mainContainer.add(cardPanel, BorderLayout.CENTER);

        MouseAdapter windowMover = new MouseAdapter() {
            private Point initialClick;
            public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = thisX + e.getX() - initialClick.x;
                int yMoved = thisY + e.getY() - initialClick.y;
                setLocation(xMoved, yMoved);
            }
        };

        titleBarPanel.addMouseListener(windowMover);
        titleBarPanel.addMouseMotionListener(windowMover);

        add(mainContainer);
    }

    public void setUsuarioActual(Usuarios usuario) {
        this.usuarioActual = usuario;
    }
    
    public Usuarios getUsuarioActual() {
        return usuarioActual;
    }
    
    public void iniciarSesion() {
        if (sesionThread == null || !sesionThread.isAlive()) {
            sesionThread = new SesionThread();
            sesionThread.start();
        }
    }
    
    public void cerrarSesion() {
        if (sesionThread != null && sesionThread.isAlive()) {
            sesionThread.detenerSesion();
        }
    }
    
    private JButton createControlButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }

    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(cardPanel, nombrePanel);
    }
}