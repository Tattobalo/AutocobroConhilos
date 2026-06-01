package autocobro.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CampoFoto extends JPanel {

    private int ancho;
    private int alto;
    private JLabel etiquetaMensaje;
    private JLabel etiquetaImagen; // 🔹 Nuevo JLabel dedicado exclusivamente a sostener y renderizar la foto
    private String rutaArchivoSeleccionado;
    private final boolean esSeleccionable;

    public CampoFoto(int ancho, int alto, boolean esSeleccionable) {
        this.ancho = ancho;
        this.alto = alto;
        this.esSeleccionable = esSeleccionable;

        // Configuración estructural obligatoria del Panel contenedor
        setPreferredSize(new Dimension(ancho, alto));
        setMinimumSize(new Dimension(ancho, alto));
        setMaximumSize(new Dimension(ancho, alto));
        setSize(ancho, alto); // Crítico para entornos con Layout Absoluto (null)
        setLayout(null); // Usamos coordenadas absolutas internas para control total de las capas
        setOpaque(false); // Para que no tape los fondos redondeados personalizados

        // 1. Inicializar la capa base: Etiqueta de Texto guía
        etiquetaMensaje = new JLabel("Click para seleccionar", SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("Arial", Font.BOLD, 12));
        etiquetaMensaje.setForeground(Color.WHITE);
        etiquetaMensaje.setBounds(0, 0, ancho, alto);
        etiquetaMensaje.setVisible(esSeleccionable);
        add(etiquetaMensaje);

        // 2. Inicializar la capa superior: Etiqueta contenedora de la Imagen real
        etiquetaImagen = new JLabel("", SwingConstants.CENTER);
        etiquetaImagen.setBounds(0, 0, ancho, alto);
        etiquetaImagen.setVisible(false);
        add(etiquetaImagen);

        // Lógica de selección del explorador de archivos de Windows
        if (esSeleccionable) {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Seleccionar foto de perfil");

                    FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de imagen (JPG, PNG)", "jpg", "jpeg", "png");
                    fileChooser.setFileFilter(filtro);

                    int resultado = fileChooser.showOpenDialog(CampoFoto.this);

                    if (resultado == JFileChooser.APPROVE_OPTION) {
                        File archivoSeleccionado = fileChooser.getSelectedFile();
                        if (archivoSeleccionado != null) {
                            rutaArchivoSeleccionado = archivoSeleccionado.getAbsolutePath();
                            System.out.println("[INTERFAZ FOTO] Cargando imagen en caliente desde: " + rutaArchivoSeleccionado);
                            cargarImagen(rutaArchivoSeleccionado);
                        }
                    }
                }
            });
        }
    }

    public void cargarImagen(String rutaImagen) {
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            try {
                File archivo = new File(rutaImagen);
                if (archivo.exists()) {
                    // 1. Leer la imagen original (sin importar resolución o si es vertical de celular)
                    BufferedImage imagenOriginal = ImageIO.read(archivo);

                    if (imagenOriginal != null) {
                        int anchoOriginal = imagenOriginal.getWidth();
                        int altoOriginal = imagenOriginal.getHeight();

                        // 2. Crear el lienzo cuadrado destino (150x150)
                        BufferedImage imagenDestinoCuadrada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = imagenDestinoCuadrada.createGraphics();

                        // Configuración de renderizado de máxima calidad
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                        // 3. ALGORITMO DE ESCALADO PROPORCIONAL (Center Crop)
                        int xDestino = 0, yDestino = 0;
                        int anchoCorte, altoCorte;

                        // Si la imagen es vertical (más alta que ancha, típica de celular)
                        if (anchoOriginal < altoOriginal) {
                            anchoCorte = anchoOriginal;
                            altoCorte = anchoOriginal; // Cortamos un cuadrado perfecto usando el ancho
                            xDestino = 0;
                            yDestino = (altoOriginal - anchoOriginal) / 2; // Centramos verticalmente el corte
                        } // Si la imagen es horizontal (más ancha que alta)
                        else {
                            anchoCorte = altoOriginal;
                            altoCorte = altoOriginal; // Cortamos un cuadrado perfecto usando el alto
                            xDestino = (anchoOriginal - altoOriginal) / 2; // Centramos horizontalmente el corte
                            yDestino = 0;
                        }

                        // 4. Dibujar la sub-sección cuadrada centrada de la foto original escalándola al tamaño del panel
                        g2d.drawImage(imagenOriginal,
                                0, 0, ancho, alto, // Esquinas del lienzo destino (0,0) a (150,150)
                                xDestino, yDestino, // Coordenadas de inicio del recorte en la foto original
                                xDestino + anchoCorte, yDestino + altoCorte, // Coordenadas de fin del recorte
                                null);

                        g2d.dispose(); // Liberar recursos gráficos de memoria

                        // 5. Actualizar los componentes de la interfaz
                        etiquetaImagen.setIcon(new ImageIcon(imagenDestinoCuadrada));
                        etiquetaMensaje.setVisible(false);
                        etiquetaImagen.setVisible(true);

                        // Forzar refresco visual en caliente
                        revalidate();
                        repaint();
                    }
                } else {
                    System.out.println("No se encontró el archivo de imagen en: " + rutaImagen);
                }
            } catch (Exception ex) {
                System.out.println("Error al procesar el escalado proporcional: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // Método solicitado para limpiar por completo la foto y restaurar el formulario a su estado por defecto
    public void restablecerImagen() {
        this.rutaArchivoSeleccionado = null;
        etiquetaImagen.setIcon(null);
        etiquetaImagen.setVisible(false);
        if (esSeleccionable) {
            etiquetaMensaje.setVisible(true);
        }
        revalidate();
        repaint();
    }

    public String getRutaArchivoSeleccionado() {
        return rutaArchivoSeleccionado;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Renderizado del contenedor estético (Fondo verde esmeralda translúcido con bordes curvos estilizados)
        g2d.setColor(new Color(255, 255, 255, 60)); // Mayor opacidad blanca para resaltar la caja guía
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));

        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));
    }
}
