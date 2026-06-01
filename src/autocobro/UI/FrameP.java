package autocobro.UI;

import autocobro.Modelos.Carrito;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import autocobro.Modelos.Usuarios;
import autocobro.Nucleo.CalculaPrecioThread;
import autocobro.Nucleo.SeleccionDeProductosThread;
import autocobro.Nucleo.SesionThread;

public class FrameP extends JFrame {

    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String REGISTRO_PANEL = "RegistroPanel";
    public static final String PRODUCTOS_PANEL = "ProductosPanel";
    public static final String MIS_PRODUCTOS_PANEL = "MisProductosPanel";
    public static final String ADMIN_PANEL = "AdminInsumosPanel"; // <-- Nueva constante de Administración

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    private SesionThread sesionThread;

    // Convertidos en atributos de clase para que los getters puedan retornarlos
    private Login loginPanel;
    private Registro registroPanel;
    private MisProductos misProductosPanel;
    private AdminInsumos adminInsumosPanel; // <-- Nuevo atributo para panel administrativo

    private Usuarios usuarioActual;
    private final Carrito carrito;

    private SeleccionDeProductosThread hilo3;
    private CalculaPrecioThread hilo4;

    public void setUsuarioActual(Usuarios usuario) {
        this.usuarioActual = usuario;
    }

    public Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    // GETTERS PÚBLICOS: Resuelven los errores de NetBeans al limpiar campos desde los hilos
    public Login getLoginPanel() {
        return loginPanel;
    }

    public Registro getRegistroPanel() {
        return registroPanel;
    }

    public MisProductos getMisProductosPanel() {
        return misProductosPanel;
    }

    public Carrito getCarrito() {
        return this.carrito;
    }

    public FrameP() {
        setTitle("AutoCobro");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);

        this.carrito = new Carrito();

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
        closeButton.addActionListener(e -> {
            detenerHilosCarrito();
            cerrarSesion();
            dispose();
            System.exit(0);
        });

        titleBarPanel.add(closeButton);
        mainContainer.add(titleBarPanel, BorderLayout.NORTH);

        // Inicialización usando los atributos de clase globales
        loginPanel = new Login(this);
        registroPanel = new Registro(this);
        Productos productosPanel = new Productos(this);
        misProductosPanel = new MisProductos(this);
        adminInsumosPanel = new AdminInsumos(this); // <-- Instancia la administración

        cardPanel.add(loginPanel, LOGIN_PANEL);
        cardPanel.add(registroPanel, REGISTRO_PANEL);
        cardPanel.add(productosPanel, PRODUCTOS_PANEL);
        cardPanel.add(misProductosPanel, MIS_PRODUCTOS_PANEL);
        cardPanel.add(adminInsumosPanel, ADMIN_PANEL); // <-- Agregado al CardLayout

        mainContainer.add(cardPanel, BorderLayout.CENTER);

        MouseAdapter windowMover = new MouseAdapter() {
            private Point initialClick;

            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            @Override
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

    public void iniciarHilosCarrito() {
        if (hilo3 == null || !hilo3.isAlive()) {
            hilo3 = new SeleccionDeProductosThread(carrito);
            hilo3.start();
        }
        if (hilo4 == null || !hilo4.isAlive()) {
            hilo4 = new CalculaPrecioThread(carrito, this);
            hilo4.start();
        }
    }

    public void detenerHilosCarrito() {
        if (hilo3 != null) {
            hilo3.detener();
        }
        if (hilo4 != null) {
            hilo4.detener();
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

        // 1. CONTROL DE FLUJO AL ENTRAR A LA PANTALLA DE REGISTRO
        if (nombrePanel.equals(REGISTRO_PANEL)) {
            if (registroPanel != null) {
                // Forzar limpieza completa de campos de texto de manera preventiva
                registroPanel.getCampoUsuario().setText("");
                registroPanel.getCampoContrasena().setText("");
                registroPanel.getCampoCorreo().setText("");

                // Forzar el restablecimiento del componente de la foto
                if (registroPanel.getCampoFotoComponente() != null) {
                    registroPanel.getCampoFotoComponente().restablecerImagen();
                }

                // Forzar a Swing a repintar el panel limpio
                registroPanel.revalidate();
                registroPanel.repaint();
            }
        } // 2. CONTROL DE FLUJO AL ENTRAR A MIS PRODUCTOS (CARRITO)
        else if (nombrePanel.equals(MIS_PRODUCTOS_PANEL)) {
            if (misProductosPanel != null) {
                misProductosPanel.mostrarProductosCarrito(); // Carga foto del usuario actual y su carrito
            }
        } // 3. CONTROL DE FLUJO GENERAL PARA CATÁLOGO
        else if (nombrePanel.equals(PRODUCTOS_PANEL)) {
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }
}
