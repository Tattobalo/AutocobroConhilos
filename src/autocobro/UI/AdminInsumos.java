package autocobro.UI;

import autocobro.Util.ConectorBD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminInsumos extends JPanel {

    private final FrameP framePrincipal;
    private JTextField txtNombreInsumo;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    
    // Componentes para la JTable dinámica
    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;

    public AdminInsumos(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
        setLayout(null); // Coordenadas absolutas globales
        setBackground(new Color(220, 220, 220));

        JPanel panelContenedor = crearPanelFormulario();
        panelContenedor.setBounds(30, 30, 740, 540); // Ampliado a 740 de ancho para el diseño dividido
        add(panelContenedor);
        
        // Carga inicial automática del inventario al instanciar el panel
        actualizarTablaInventario();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 102, 204), 0, getHeight(), new Color(10, 35, 80));
                g2d.setPaint(gp);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        // Título de la sección administrativa
        JLabel lblTitulo = new JLabel("Control de Inventario y Alta de Insumos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 20, 700, 30);
        panel.add(lblTitulo);

        // ================= COLUMNA IZQUIERDA: VISUALIZACIÓN DEL INVENTARIO =================
        String[] columnas = {"Producto", "Presentación / Descripción", "Precio ($)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición directa sobre las celdas para proteger el hilo
            }
        };
        
        tablaInventario = new JTable(modeloTabla);
        tablaInventario.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollTabla = new JScrollPane(tablaInventario);
        scrollTabla.setBounds(30, 80, 400, 370);
        panel.add(scrollTabla);

        // EVENTO CLAVE: Cargar datos de la fila seleccionada en las cajas de texto de la derecha
        tablaInventario.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = tablaInventario.getSelectedRow();
            if (filaSeleccionada != -1) {
                txtNombreInsumo.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                txtDescripcion.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
                txtPrecio.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            }
        });

        // ================= COLUMNA DERECHA: FORMULARIO DE EDICIÓN RAPIDA =================
        int xForm = 460; // Desplazamiento horizontal de la columna derecha

        JLabel lblNombre = new JLabel("Nombre del Producto:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        lblNombre.setBounds(xForm, 80, 250, 20);
        panel.add(lblNombre);

        txtNombreInsumo = new CampoTextoRedondeado(20);
        txtNombreInsumo.setBounds(xForm, 105, 250, 30);
        panel.add(txtNombreInsumo);

        JLabel lblDesc = new JLabel("Presentación / Descripción:");
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(new Font("Arial", Font.BOLD, 13));
        lblDesc.setBounds(xForm, 155, 250, 20);
        panel.add(lblDesc);

        txtDescripcion = new CampoTextoRedondeado(20);
        txtDescripcion.setBounds(xForm, 180, 250, 30);
        panel.add(txtDescripcion);

        JLabel lblPrecio = new JLabel("Precio Unitario ($):");
        lblPrecio.setForeground(Color.WHITE);
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrecio.setBounds(xForm, 230, 250, 20);
        panel.add(lblPrecio);

        txtPrecio = new CampoTextoRedondeado(20);
        txtPrecio.setBounds(xForm, 255, 250, 30);
        panel.add(txtPrecio);

        // ================= SECCIÓN DE CONTROL: BOTONES OPERATIVOS =================
        JButton btnGuardar = new BotonRedondeado("Registrar Insumo", new Color(153, 51, 255));
        btnGuardar.setBounds(xForm, 310, 250, 35);
        panel.add(btnGuardar);

        JButton btnActualizar = new BotonRedondeado("Actualizar Precio", new Color(0, 153, 204));
        btnActualizar.setBounds(xForm, 355, 250, 35);
        panel.add(btnActualizar);

        JButton btnEliminar = new BotonRedondeado("Eliminar Insumo", new Color(204, 51, 51));
        btnEliminar.setBounds(xForm, 400, 250, 35);
        panel.add(btnEliminar);

        // Botón de salida posicionado en la parte baja central
        JButton btnVolver = new BotonRedondeado("Cerrar Sesión", new Color(100, 100, 100));
        btnVolver.setBounds(270, 475, 200, 40);
        panel.add(btnVolver);

        // 🔹 1. OPERACIÓN: INSERTAR NUEVO INSUMO RELACIONAL
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombreInsumo.getText().trim();
            String desc = txtDescripcion.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (nombre.isEmpty() || desc.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new Thread(() -> {
                String queryProducto = "INSERT INTO productos (nombre) VALUES (?)";
                String queryPresentacion = "INSERT INTO precios_presentaciones (producto_id, descripcion, precio) VALUES (?, ?, ?)";
                try (Connection con = ConectorBD.conectar()) {
                    con.setAutoCommit(false);
                    int productoId = -1;
                    try (PreparedStatement psProd = con.prepareStatement(queryProducto, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        psProd.setString(1, nombre);
                        psProd.executeUpdate();
                        try (ResultSet rs = psProd.getGeneratedKeys()) {
                            if (rs.next()) productoId = rs.getInt(1);
                        }
                    }
                    if (productoId != -1) {
                        try (PreparedStatement psPres = con.prepareStatement(queryPresentacion)) {
                            psPres.setInt(1, productoId);
                            psPres.setString(2, desc);
                            psPres.setDouble(3, Double.parseDouble(precioStr));
                            psPres.executeUpdate();
                        }
                        con.commit();
                        actualizarTablaInventario(); // Refrescar vista
                        limpiarCampos();
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "¡Insumo registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE));
                    } else {
                        con.rollback();
                    }
                } catch (SQLException ex) {
                    System.out.println("[HILO ADMIN] Error al insertar: " + ex.getMessage());
                }
            }).start();
        });

        // 🔹 2. OPERACIÓN: ACTUALIZAR PRECIO
        btnActualizar.addActionListener(e -> {
            String desc = txtDescripcion.getText().trim();
            String precioStr = txtPrecio.getText().trim();

            if (desc.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para cargar la descripción de la presentación.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new Thread(() -> {
                String sql = "UPDATE precios_presentaciones SET precio = ? WHERE descripcion = ?";
                try (Connection con = ConectorBD.conectar();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setDouble(1, Double.parseDouble(precioStr));
                    ps.setString(2, desc);
                    int filas = ps.executeUpdate();
                    if (filas > 0) {
                        actualizarTablaInventario(); // Refrescar vista
                        limpiarCampos();
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "¡Precio actualizado correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE));
                    }
                } catch (SQLException ex) {
                    System.out.println("[HILO ADMIN] Error al actualizar: " + ex.getMessage());
                }
            }).start();
        });

        // 🔹 3. OPERACIÓN: ELIMINACIÓN SECUENCIAL EN CASCADA
        btnEliminar.addActionListener(e -> {
            String desc = txtDescripcion.getText().trim();

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona el insumo de la tabla que deseas eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas remover este insumo y sus dependencias?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmar != JOptionPane.YES_OPTION) return;

            new Thread(() -> {
                String selectId = "SELECT producto_id FROM precios_presentaciones WHERE descripcion = ?";
                String deletePresentacion = "DELETE FROM precios_presentaciones WHERE descripcion = ?";
                String deleteProducto = "DELETE FROM productos WHERE id = ?";
                try (Connection con = ConectorBD.conectar()) {
                    con.setAutoCommit(false);
                    int idPadre = -1;
                    try (PreparedStatement psSel = con.prepareStatement(selectId)) {
                        psSel.setString(1, desc);
                        try (ResultSet rs = psSel.executeQuery()) {
                            if (rs.next()) idPadre = rs.getInt("producto_id");
                        }
                    }
                    if (idPadre != -1) {
                        try (PreparedStatement psDelPres = con.prepareStatement(deletePresentacion)) {
                            psDelPres.setString(1, desc);
                            psDelPres.executeUpdate();
                        }
                        try (PreparedStatement psDelProd = con.prepareStatement(deleteProducto)) {
                            psDelProd.setInt(1, idPadre);
                            psDelProd.executeUpdate();
                        }
                        con.commit();
                        actualizarTablaInventario(); // Refrescar vista
                        limpiarCampos();
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Insumo removido por completo.", "Éxito", JOptionPane.INFORMATION_MESSAGE));
                    } else {
                        con.rollback();
                    }
                } catch (SQLException ex) {
                    System.out.println("[HILO ADMIN] Error en borrado en cascada: " + ex.getMessage());
                }
            }).start();
        });

        // Regresar de forma segura al Login
        btnVolver.addActionListener(e -> {
            framePrincipal.cerrarSesion();
            framePrincipal.mostrarPanel(FrameP.LOGIN_PANEL);
        });

        return panel;
    }

    // 🔹 METODO CONCURRENTE: Consulta relacional INNER JOIN para cargar el inventario real en caliente
    public void actualizarTablaInventario() {
        new Thread(() -> {
            System.out.println("[HILO REFRESH] Sincronizando JTable con XAMPP...");
            String sql = "SELECT p.nombre, pr.descripcion, pr.precio " +
                         "FROM productos p " +
                         "INNER JOIN precios_presentaciones pr ON p.id = pr.producto_id";
            
            try (Connection con = ConectorBD.conectar();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                // Limpiar filas previas para evitar duplicidad visual
                SwingUtilities.invokeLater(() -> modeloTabla.setRowCount(0));

                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    double precio = rs.getDouble("precio");

                    // Inyectar datos fila por fila al hilo de eventos de Swing
                    SwingUtilities.invokeLater(() -> {
                        modeloTabla.addRow(new Object[]{nombre, descripcion, precio});
                    });
                }
            } catch (SQLException ex) {
                System.out.println("[HILO REFRESH] Error al cargar inventario: " + ex.getMessage());
            }
        }).start();
    }

    private void limpiarCampos() {
        SwingUtilities.invokeLater(() -> {
            txtNombreInsumo.setText("");
            txtDescripcion.setText("");
            txtPrecio.setText("");
            tablaInventario.clearSelection();
        });
    }
}