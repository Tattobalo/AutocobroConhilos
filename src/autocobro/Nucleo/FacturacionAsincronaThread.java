package autocobro.Nucleo;

import autocobro.Modelos.Carrito;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.UUID;

public class FacturacionAsincronaThread extends Thread {

    private final Carrito carrito;
    private final String nombreUsuario;
    // Dentro de FacturacionAsincronaThread.java declarar:
    private String codigoFacturacion;

    public FacturacionAsincronaThread(Carrito carrito, String nombreUsuario) {
        this.carrito = carrito;
        this.nombreUsuario = nombreUsuario;
        // Al generar el código dentro del run() guardarlo en la variable global:
        this.codigoFacturacion = "FC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public void run() {
        System.out.println("[HILO 8 - FACTURACIÓN] Generando token de facturación autónoma...");

        try {
            // Simulamos el retraso del algoritmo de empaquetado seguro (1 segundo)
            Thread.sleep(1000);

            // Generar un código único de 8 dígitos para la factura (ej: FC-A8F29C3B)
            String codigoFacturacion = "FC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Crear el archivo de texto que simula el servidor de Base de Datos Web de LiPaDaS
            File archivoWeb = new File("servidor_facturas_web.txt");

            try (PrintWriter writer = new PrintWriter(new FileWriter(archivoWeb, true))) {
                writer.println("Código Generado: " + codigoFacturacion
                        + " | Cliente: " + nombreUsuario
                        + " | Monto Reservado SAT: $" + String.format("%.2f", carrito.getTotal() * 1.16)
                        + " | Estado: PENDIENTE DE TIMBRADO WEB");
            }

            System.out.println("[HILO 8 - FACTURACIÓN] Código de Factura creado con éxito: " + codigoFacturacion);
            System.out.println("[HILO 8 - FACTURACIÓN] Datos guardados asíncronamente en la nube de LiPaDaS.");

        } catch (Exception e) {
            System.err.println("[HILO 8 - FACTURACIÓN] Error en la persistencia del token fiscal.");
        }
    }
    // Y al final del archivo añadir el getter:
    public String getCodigoFacturacion() {
        return this.codigoFacturacion;
    }
}
