package autocobro.Nucleo;

import autocobro.Modelos.Carrito;
import autocobro.Modelos.ProductoSeleccionado;
import autocobro.Modelos.Usuarios;

import java.awt.Desktop;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class GenerarTicketThread extends Thread {

    private final Carrito carrito;
    private final Usuarios usuario;

    public GenerarTicketThread(Carrito carrito, Usuarios usuario) {
        this.carrito = carrito;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        try {
            // Nombre de archivo único por usuario
            String nombreArchivo = "ticket_" + usuario.getNombreUsuario() + ".txt";
            File file = new File(nombreArchivo);

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("----- TICKET DE COMPRA -----");
                writer.println("Usuario: " + usuario.getNombreUsuario());
                writer.println("Correo: " + usuario.getCorreo());
                writer.println();
                writer.println("Productos:");

                for (ProductoSeleccionado p : carrito.getProductos()) {
                    writer.println(p.getNombre() + " | Cantidad: " + p.getCantidad() +
                                   " | Subtotal: $" + String.format("%.2f", p.getSubtotal()));
                }

                double total = carrito.getTotal();
                double totalConDescuento = total * 0.9; // ejemplo: 10% de descuento
                writer.println("----------------------------");
                writer.println("TOTAL: $" + String.format("%.2f", total));
                writer.println("TOTAL CON DESCUENTO: $" + String.format("%.2f", totalConDescuento));
                writer.println("----------------------------");
            }

            System.out.println("[HILO 7] Ticket generado en: " + file.getAbsolutePath());
            
            // Abrir automáticamente el archivo
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("[HILO 7] No se puede abrir el archivo automáticamente.");
            }
            

        } catch (Exception e) {
            System.out.println("[HILO 7] Error generando ticket.");
            e.printStackTrace();
        }
    }
}
/*

package autocobro.Nucleo;

import autocobro.Modelos.Carrito;
import autocobro.Modelos.Usuarios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GenerarTicketThread extends Thread {

    private final Carrito carrito;
    private final Usuarios usuario;

    public GenerarTicketThread(Carrito carrito, Usuarios usuario) {
        this.carrito = carrito;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        try {
            // Nombre de archivo PDF único por usuario
            String nombreArchivo = "ticket_" + usuario.getNombreUsuario() + ".pdf";
            File file = new File(nombreArchivo);

            try (OutputStream out = new FileOutputStream(file)) {
                // Escribir contenido muy básico de PDF
                String contenido = "%PDF-1.4\n" +
                        "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n" +
                        "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n" +
                        "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R >>\nendobj\n" +
                        "4 0 obj\n<< /Length 44 >>\nstream\n" +
                        "BT /F1 24 Tf 50 750 Td (TICKET DE COMPRA) Tj ET\nendstream\nendobj\n" +
                        "xref\n0 5\n0000000000 65535 f \n0000000010 00000 n \n0000000053 00000 n \n" +
                        "0000000100 00000 n \n0000000200 00000 n \ntrailer << /Size 5 /Root 1 0 R >>\nstartxref\n300\n%%EOF";

                // Para simplificar, escribimos solo texto plano como PDF
                out.write(contenido.getBytes());
            }

            System.out.println("[HILO 7] PDF generado en: " + file.getAbsolutePath());

            // Abrir PDF automáticamente
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(file);
            } else {
                System.out.println("[HILO 7] No se puede abrir el PDF automáticamente.");
            }

        } catch (Exception e) {
            System.out.println("[HILO 7] Error generando PDF.");
            e.printStackTrace();
        }
    }
}

*/

