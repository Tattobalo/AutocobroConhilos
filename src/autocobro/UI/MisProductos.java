package autocobro.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class MisProductos extends JPanel {

    private final FrameP framePrincipal;

    public MisProductos(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(new BorderLayout());
        setBackground(new Color(220, 220, 220));

        // Panel lateral con gradiente
        JPanel panelLateral = crearPanelLateral();
        add(panelLateral, BorderLayout.WEST);

        // Panel principal de contenido
        JPanel panelContenido = crearPanelContenido();
        add(panelContenido, BorderLayout.CENTER);
    }

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
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(150, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Botón/Label "Agregar Productos"
        JLabel agregarProductosLabel = new JLabel("Agregar Productos");
        agregarProductosLabel.setForeground(Color.WHITE);
        agregarProductosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        agregarProductosLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(agregarProductosLabel, gbc);
        
        agregarProductosLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                framePrincipal.mostrarPanel(FrameP.PRODUCTOS_PANEL);
            }
        });

        // Separador visual
        JPanel separador = new JPanel();
        separador.setPreferredSize(new Dimension(100, 2));
        separador.setBackground(Color.WHITE);
        gbc.gridy = 1;
        panel.add(separador, gbc);

        // Botón/Label "Mis Productos"
        JLabel misProductosLabel = new JLabel("Mis Productos");
        misProductosLabel.setForeground(Color.WHITE);
        misProductosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        misProductosLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 2;
        panel.add(misProductosLabel, gbc);

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
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(650, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        
        // Título "Mis Productos"
        JLabel titulo = new JLabel("Mis Productos");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(titulo, gbc);
        
        // --- Encabezados de la tabla ---
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 5, 10);
        
        JLabel nombreHeader = new JLabel("Nombre");
        nombreHeader.setForeground(Color.WHITE);
        gbc.gridx = 0; panel.add(nombreHeader, gbc);

        JLabel descripcionHeader = new JLabel("Descripción");
        descripcionHeader.setForeground(Color.WHITE);
        gbc.gridx = 1; panel.add(descripcionHeader, gbc);

        JLabel cantidadHeader = new JLabel("Cantidad");
        cantidadHeader.setForeground(Color.WHITE);
        gbc.gridx = 2; panel.add(cantidadHeader, gbc);

        JLabel precioHeader = new JLabel("Precio");
        precioHeader.setForeground(Color.WHITE);
        gbc.gridx = 3; panel.add(precioHeader, gbc);

        // --- Filas de la tabla (ejemplo) ---
        // Aquí irían los JLabels con los datos de los productos seleccionados
        // Se puede usar un bucle para agregarlos dinámicamente

        // --- Sección de precio final y botón ---
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        
        JLabel precioFinalLabel = new JLabel("Precio final:");
        precioFinalLabel.setForeground(Color.WHITE);
        gbc.gridx = 2;
        gbc.insets = new Insets(30, 10, 10, 5);
        panel.add(precioFinalLabel, gbc);
        
        JTextField precioFinalField = new JTextField(10);
        precioFinalField.setEditable(false);
        precioFinalField.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 3;
        gbc.insets = new Insets(30, 5, 10, 10);
        panel.add(precioFinalField, gbc);
        
        JButton botonPagar = new BotonRedondeado("Pagar", new Color(153, 51, 255));
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(botonPagar, gbc);

        return panel;
    }
}