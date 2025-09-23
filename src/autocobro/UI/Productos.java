package autocobro.UI;

import autocobro.Util.ConectorBD;
import autocobro.Modelos.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Productos extends JPanel {

    private FrameP framePrincipal;
    private JPanel tablaContenido;

    public Productos(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(null); // uso de coordenadas absolutas
        setBackground(new Color(220, 220, 220));

        JPanel panelPrincipal = crearPanelContenido();
        panelPrincipal.setBounds(10, 20, 780, 540); // ocupa casi todo el JFrame
        add(panelPrincipal);

        cargarProductos();
    }

    private JPanel crearPanelContenido() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(0, 204, 153);   // verde
                Color color2 = new Color(60, 60, 60);   // gris oscuro
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                int cornerRadius = 30;
                RoundRectangle2D roundedRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
                g2d.fill(roundedRect);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        // --- Título ---
        JLabel titulo = new JLabel("Productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.BLACK);
        titulo.setBounds(30, 20, 200, 40);
        panel.add(titulo);

        // --- Botón "Mis Productos" ---
        JButton botonMisProductos = crearBoton("Mis Productos");
        botonMisProductos.setBounds(600, 20, 120, 30);
        panel.add(botonMisProductos);
        botonMisProductos.addActionListener(e -> {
            framePrincipal.mostrarPanel(FrameP.MIS_PRODUCTOS_PANEL);
        });

        // --- Botón Categorías ---
        JButton botonCategorias = crearBoton("Categorías");
        botonCategorias.setBounds(30, 70, 100, 30);
        panel.add(botonCategorias);

        // --- Label Categoría ---
        JLabel nombreCategoriaLabel = new JLabel("(Nombre categoría)");
        nombreCategoriaLabel.setForeground(Color.BLACK);
        nombreCategoriaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nombreCategoriaLabel.setBounds(150, 70, 200, 30);
        panel.add(nombreCategoriaLabel);

        // --- Encabezados ---
        int yHeaders = 120;
        panel.add(crearHeader("Nombre", 30, yHeaders, 150, 25));
        panel.add(crearHeader("Descripción", 190, yHeaders, 200, 25));
        panel.add(crearHeader("Cantidad", 400, yHeaders, 100, 25));
        panel.add(crearHeader("Precio", 510, yHeaders, 100, 25));

        // --- Panel tabla con scroll ---
        tablaContenido = new JPanel();
        tablaContenido.setOpaque(false);
        tablaContenido.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(tablaContenido);
        scrollPane.setBounds(30, 150, 600, 300);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        // --- Botón Agregar ---
        JButton botonAgregar = crearBoton("Agregar");
        botonAgregar.setBounds(530, 470, 100, 30);
        panel.add(botonAgregar);

        return panel;
    }

    private JLabel crearHeader(String texto, int x, int y, int w, int h) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, w, h);
        return label;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(180, 40, 180));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        return boton;
    }

    private void cargarProductos() {
        try (Connection conexion = ConectorBD.conectar()) {
            String query = "SELECT p.nombre, pp.descripcion, pp.precio FROM productos p " +
                    "JOIN precios_presentaciones pp ON p.id = pp.producto_id";

            try (PreparedStatement pstmt = conexion.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                List<Producto> productos = new ArrayList<>();
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    double precio = rs.getDouble("precio");

                    productos.add(new Producto(nombre, descripcion, precio));
                }
                mostrarProductos(productos);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarProductos(List<Producto> productos) {
        tablaContenido.removeAll();

        int y = 10;
        for (Producto producto : productos) {
            JTextField nombre = crearCelda(producto.getNombre(), 30, y, 150, 25);
            JTextField descripcion = crearCelda(producto.getDescripcion(), 190, y, 200, 25);
            JTextField cantidad = crearCelda("0", 400, y, 100, 25);
            cantidad.setEditable(true);
            JTextField precio = crearCelda("$" + String.format("%.2f", producto.getPrecio()), 510, y, 100, 25);

            tablaContenido.add(nombre);
            tablaContenido.add(descripcion);
            tablaContenido.add(cantidad);
            tablaContenido.add(precio);

            y += 35;
        }

        tablaContenido.setPreferredSize(new Dimension(600, y));
        tablaContenido.revalidate();
        tablaContenido.repaint();
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
