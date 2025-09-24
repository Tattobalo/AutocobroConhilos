package autocobro.UI;

import autocobro.Modelos.Usuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class MisProductos extends JPanel {

    private final FrameP framePrincipal;
    private CampoFoto campoFoto;

    private JPanel tablaContenido;

    private JTextField precioFinalField;
    private JTextField precioConDescuentoField;

    public MisProductos(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(null);
        setBackground(new Color(220, 220, 220));

        JPanel panelLateral = crearPanelLateral();
        panelLateral.setBounds(10, 10, 170, 550);
        add(panelLateral);

        JPanel panelContenido = crearPanelContenido();
        panelContenido.setBounds(190, 10, 600, 550);
        add(panelContenido);

        //SwingUtilities.invokeLater(this::mostrarFotoUsuario);
    }

    /*private void mostrarFotoUsuario() {
        Usuarios usuario = framePrincipal.getUsuarioActual();
        if (usuario != null && usuario.getRutaFotoPerfil() != null && !usuario.getRutaFotoPerfil().isEmpty()) {
            String rutaCompleta = "Images" + File.separator + usuario.getRutaFotoPerfil();
            campoFoto.cargarImagen(rutaCompleta);
        }
    }*/
    private JPanel crearPanelLateral() {
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
                int cornerRadius = 30;
                RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                g2d.fill(roundedRect);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        campoFoto = new CampoFoto(150, 150, false);
        campoFoto.setBounds(10, 10, 150, 150);
        campoFoto.setOpaque(false);
        panel.add(campoFoto);

        JPanel separador = new JPanel();
        separador.setBounds(10, 170, 150, 2);
        separador.setBackground(Color.WHITE);
        panel.add(separador);

        JLabel agregarProductosLabel = new JLabel("Agregar Productos");
        agregarProductosLabel.setForeground(Color.WHITE);
        agregarProductosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        agregarProductosLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        agregarProductosLabel.setBounds(10, 182, 150, 30);
        panel.add(agregarProductosLabel);

        agregarProductosLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                // Limpiar carrito antes de ir al panel de productos
                framePrincipal.getCarrito().limpiarCarrito();

                framePrincipal.mostrarPanel(FrameP.PRODUCTOS_PANEL);
            }
        });

        return panel;
    }

    private JPanel crearPanelContenido() {
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
                int cornerRadius = 30;
                RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                g2d.fill(roundedRect);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        JLabel titulo = new JLabel("Mis Productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(20, 20, 400, 40);
        panel.add(titulo);

        JLabel nombreHeader = new JLabel("Nombre");
        nombreHeader.setForeground(Color.WHITE);
        nombreHeader.setBounds(20, 80, 100, 30);
        panel.add(nombreHeader);

        JLabel descripcionHeader = new JLabel("Descripción");
        descripcionHeader.setForeground(Color.WHITE);
        descripcionHeader.setBounds(170, 80, 100, 30);
        panel.add(descripcionHeader);

        JLabel cantidadHeader = new JLabel("Cantidad");
        cantidadHeader.setForeground(Color.WHITE);
        cantidadHeader.setBounds(320, 80, 100, 30);
        panel.add(cantidadHeader);

        JLabel precioHeader = new JLabel("Precio");
        precioHeader.setForeground(Color.WHITE);
        precioHeader.setBounds(470, 80, 100, 30);
        panel.add(precioHeader);

        // Aquí va la tabla de productos
        tablaContenido = new JPanel();
        tablaContenido.setOpaque(false);
        tablaContenido.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(tablaContenido);
        scrollPane.setBounds(20, 120, 560, 300);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        JLabel precioFinalLabel = new JLabel("Precio normal:");
        precioFinalLabel.setForeground(Color.WHITE);
        precioFinalLabel.setBounds(20, 460, 120, 30);
        panel.add(precioFinalLabel);

        // ahora sí usamos el atributo de clase
        precioFinalField = new JTextField();
        precioFinalField.setEditable(false);
        precioFinalField.setBounds(150, 460, 100, 30);
        panel.add(precioFinalField);

        // Precio con descuento
        JLabel precioDescuentoLabel = new JLabel("Precio con descuento:");
        precioDescuentoLabel.setForeground(Color.WHITE);
        precioDescuentoLabel.setBounds(270, 460, 150, 30);
        panel.add(precioDescuentoLabel);

        precioConDescuentoField = new JTextField();
        precioConDescuentoField.setEditable(false);
        precioConDescuentoField.setBounds(420, 460, 100, 30);
        panel.add(precioConDescuentoField);

        JButton botonPagar = new BotonRedondeado("Pagar", new Color(153, 51, 255));
        botonPagar.setBounds(470, 500, 100, 40);
        panel.add(botonPagar);

        botonPagar.addActionListener(e -> {
            framePrincipal.detenerHilosCarrito(); //detenemos hilo 3 y 4
            framePrincipal.cerrarSesion();  // detenemos el hilo 2
            framePrincipal.getCarrito().getProductos().clear(); // opcional, limpiar carrito
            framePrincipal.mostrarPanel(FrameP.LOGIN_PANEL); // volver al login
        });

        return panel;
    }

    public void actualizarPrecios() {
        mostrarProductosCarrito();
        double total = framePrincipal.getCarrito().getTotal();
        double totalConDescuento = total * 0.9; // ejemplo: 10% de descuento
        precioConDescuentoField.setText("$" + String.format("%.2f", totalConDescuento));

        // Actualiza ambos campos
        precioFinalField.setText("$" + String.format("%.2f", total));
        precioConDescuentoField.setText("$" + String.format("%.2f", totalConDescuento));

    }

    public void mostrarProductosCarrito() {

        Usuarios usuario = framePrincipal.getUsuarioActual();
        if (usuario != null && usuario.getRutaFotoPerfil() != null && !usuario.getRutaFotoPerfil().isEmpty()) {
            String rutaCompleta = "Images" + File.separator + usuario.getRutaFotoPerfil();
            campoFoto.cargarImagen(rutaCompleta);
        }

        tablaContenido.removeAll();
        tablaContenido.revalidate();
        tablaContenido.setLayout(null);

        java.util.List<autocobro.Modelos.ProductoSeleccionado> productos = framePrincipal.getCarrito().getProductos();

        int y = 10;
        double total = 0;

        for (autocobro.Modelos.ProductoSeleccionado producto : productos) {
            JPanel fila = new JPanel();
            fila.setLayout(null);
            fila.setOpaque(false);
            fila.setBounds(0, y, 540, 30);

            JTextField nombre = crearCelda(producto.getNombre(), 0, 0, 150, 25);
            JTextField descripcion = crearCelda(producto.getDescripcion(), 160, 0, 200, 25);
            JTextField cantidad = crearCelda(String.valueOf(producto.getCantidad()), 370, 0, 50, 25);
            JTextField precio = crearCelda("$" + String.format("%.2f", producto.getPrecio() * producto.getCantidad()), 430, 0, 100, 25);

            fila.add(nombre);
            fila.add(descripcion);
            fila.add(cantidad);
            fila.add(precio);

            tablaContenido.add(fila);
            y += 35;

            total += producto.getCantidad() * producto.getPrecio();
        }

        tablaContenido.setPreferredSize(new Dimension(540, y));
        tablaContenido.revalidate();
        tablaContenido.repaint();

        precioFinalField.setText("$" + String.format("%.2f", total));
    }

    private JTextField crearCelda(String texto, int x, int y, int w, int h) {
        JTextField campo = new JTextField(texto);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setEditable(false);
        campo.setBackground(new Color(200, 200, 200));
        campo.setBounds(x, y, w, h);
        return campo;
    }
}
