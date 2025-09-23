package autocobro.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CampoFoto extends JPanel {

    private BufferedImage imagenPerfil;
    private int ancho;
    private int alto;
    private JLabel etiquetaMensaje;
    private String rutaArchivoSeleccionado;

    public CampoFoto(int ancho, int alto, boolean esSeleccionable) {
        this.ancho = ancho;
        this.alto = alto;
        setPreferredSize(new Dimension(ancho, alto));
        setMinimumSize(new Dimension(ancho, alto));
        setMaximumSize(new Dimension(ancho, alto));
        setLayout(new GridBagLayout());

        etiquetaMensaje = new JLabel("Clic para agregar foto");
        etiquetaMensaje.setForeground(Color.WHITE);
        add(etiquetaMensaje);

        // La lógica del mouse solo se agrega si el componente es seleccionable
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
                        // Obtenemos solo el nombre del archivo
                        rutaArchivoSeleccionado = archivoSeleccionado.getName();

                        // La lógica para cargar la imagen necesita la ruta completa temporalmente
                        cargarImagen(archivoSeleccionado.getAbsolutePath());
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
                    imagenPerfil = ImageIO.read(archivo);
                    etiquetaMensaje.setVisible(false);
                    repaint();
                } else {
                    System.out.println("No se encontró el archivo en la ruta: " + rutaImagen);
                }
            } catch (Exception ex) {
                System.out.println("Error al cargar la imagen: " + ex.getMessage());
            }
        }
    }

    public String getRutaArchivoSeleccionado() {
        return rutaArchivoSeleccionado;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ancho, alto);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(ancho, alto);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));

        g2d.setColor(Color.WHITE);
        g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 30, 30));

        if (imagenPerfil != null) {
            Image imagenEscalada = imagenPerfil.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            g2d.drawImage(imagenEscalada, 0, 0, this);
        }
    }
}
