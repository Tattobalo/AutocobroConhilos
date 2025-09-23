package autocobro.UI;

import autocobro.Modelos.Producto;
import autocobro.Util.ConectorBD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingConstants.CENTER;

public class Productos extends JPanel {

    private FrameP framePrincipal;
    private JPanel tablaContenido;

    public Productos(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(new GridBagLayout());
        setBackground(new Color(220, 220, 220));

        JPanel panelPrincipal = crearPanelContenido();
        add(panelPrincipal, new GridBagConstraints());

        cargarProductos();
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
        panel.setLayout(new GridBagLayout());
        // 👇 más grande, casi del tamaño del JFrame (800x600)
        panel.setPreferredSize(new Dimension(850, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titulo = new JLabel("Productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(titulo, gbc);

        JButton botonMisProductos = new BotonRedondeado("Mis Productos", new Color(153, 51, 255));
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 20, 20, 20); gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(botonMisProductos, gbc);

        botonMisProductos.addActionListener(e -> {
            framePrincipal.mostrarPanel(FrameP.MIS_PRODUCTOS_PANEL);
        });

        JButton botonCategorias = new BotonRedondeado("Categorias", new Color(153, 51, 255));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.insets = new Insets(10, 20, 10, 5);
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        panel.add(botonCategorias, gbc);

        JLabel nombreCategoriaLabel = new JLabel("(Nombre categoria)");
        nombreCategoriaLabel.setForeground(Color.WHITE);
        nombreCategoriaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 20); gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nombreCategoriaLabel, gbc);

        JPanel headersPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        headersPanel.setOpaque(false);
        headersPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel nombreHeader = new JLabel("Nombre", CENTER);
        nombreHeader.setForeground(Color.WHITE);
        headersPanel.add(nombreHeader);

        JLabel descripcionHeader = new JLabel("Descripción", CENTER);
        descripcionHeader.setForeground(Color.WHITE);
        headersPanel.add(descripcionHeader);

        JLabel cantidadHeader = new JLabel("Cantidad", CENTER);
        cantidadHeader.setForeground(Color.WHITE);
        headersPanel.add(cantidadHeader);

        JLabel precioHeader = new JLabel("Precio", CENTER);
        precioHeader.setForeground(Color.WHITE);
        headersPanel.add(precioHeader);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(20, 20, 5, 20);
        panel.add(headersPanel, gbc);

        tablaContenido = new JPanel();
        tablaContenido.setOpaque(false);
        tablaContenido.setLayout(new BoxLayout(tablaContenido, BoxLayout.Y_AXIS));

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH; gbc.insets = new Insets(0, 20, 20, 20);

        JScrollPane scrollPane = new JScrollPane(tablaContenido);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        // 👇 deshabilitamos scroll horizontal
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, gbc);

        JButton botonAgregar = new BotonRedondeado("Agregar", new Color(153, 51, 255));
        gbc.gridx = 3; gbc.gridy = 4; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; gbc.insets = new Insets(30, 20, 20, 20);
        panel.add(botonAgregar, gbc);

        return panel;
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
        tablaContenido.revalidate();

        for (Producto producto : productos) {
            JPanel fila = new JPanel();
            fila.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            fila.setOpaque(false);
            // 👇 cada fila ocupará casi todo el ancho del panel
            fila.setPreferredSize(new Dimension(700, 40));

            JLabel nombre = new JLabel(producto.getNombre(), CENTER);
            nombre.setPreferredSize(new Dimension(180, 30));
            nombre.setHorizontalAlignment(SwingConstants.CENTER);
            nombre.setForeground(Color.WHITE);
            fila.add(nombre);

            JLabel descripcion = new JLabel(producto.getDescripcion(), CENTER);
            descripcion.setPreferredSize(new Dimension(250, 30));
            descripcion.setHorizontalAlignment(SwingConstants.CENTER);
            descripcion.setForeground(Color.WHITE);
            fila.add(descripcion);

            JTextField cantidad = new JTextField("0", 5);
            cantidad.setHorizontalAlignment(JTextField.CENTER);
            cantidad.setEditable(false);
            cantidad.setPreferredSize(new Dimension(80, 30));
            fila.add(cantidad);

            JLabel precio = new JLabel("$" + String.format("%.2f", producto.getPrecio()), CENTER);
            precio.setPreferredSize(new Dimension(120, 30));
            precio.setHorizontalAlignment(SwingConstants.CENTER);
            precio.setForeground(Color.WHITE);
            fila.add(precio);

            fila.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cantidad.setEditable(true);
                }
            });

            tablaContenido.add(fila);
        }

        tablaContenido.repaint();
    }
}
