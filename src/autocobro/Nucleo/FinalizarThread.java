package autocobro.Nucleo;

import autocobro.UI.FrameP;

public class FinalizarThread extends Thread {

    private final FrameP framePrincipal;
    private final boolean requiereFactura;

    // Constructor actualizado para recibir la decisión de la UI
    public FinalizarThread(FrameP framePrincipal, boolean requiereFactura) {
        this.framePrincipal = framePrincipal;
        this.requiereFactura = requiereFactura;
    }

    @Override
    public void run() {
        // Doble validación de seguridad por si acaso
        System.out.println("[HILO 6] Procesando transacción financiera de manera atómica...");

        try {
            // 1. Simular el procesamiento del pago bancario (fase de autorización)
            Thread.sleep(2000);

            // 2. Detener hilos de monitoreo en segundo plano
            framePrincipal.detenerHilosCarrito();
            framePrincipal.cerrarSesion();

            // 3. DISPARAR HILO 7: GENERACIÓN DE TICKET LOCAL (Siempre se genera)
            GenerarTicketThread hilo7 = new GenerarTicketThread(
                    framePrincipal.getCarrito(),
                    framePrincipal.getUsuarioActual()
            );
            hilo7.start();

            // 4. FLUJO CONDICIONAL ASÍNCRONO: FACTURACIÓN ELECTRÓNICA Y CORREO
            FacturacionAsincronaThread hilo8 = null;
            if (requiereFactura) {
                System.out.println("[HILO 6] Usuario solicitó factura. Inicializando Hilo 8...");
                hilo8 = new FacturacionAsincronaThread(
                        framePrincipal.getCarrito(),
                        framePrincipal.getUsuarioActual().getNombreUsuario()
                );
                hilo8.start();
            }

            // 5. CONTROL DE CONCURRENCIA MEDIANTE BARRERAS SEGURAS (join)
            try {
                hilo7.join(); // Esperar ticket local

                if (hilo8 != null) {
                    hilo8.join(); // Esperar a que se genere el código alfanumérico fiscal del Hilo 8

                    // Extraemos los datos necesarios directo de la sesión en RAM del Frame Principal
                    String tokenFiscal = hilo8.getCodigoFacturacion(); // Asegúrate de tener un método getter en tu Hilo 8 que retorne la variable del código generado
                    String correoCliente = framePrincipal.getUsuarioActual().getCorreo();
                    String nombreCliente = framePrincipal.getUsuarioActual().getNombreUsuario();

                    // 🔹 DISPARO CONCURRENTE DEL CORREO: Arranca en paralelo sin demorar al cliente
                    EnviarCorreoThread hiloMail = new EnviarCorreoThread(
                            correoCliente,
                            nombreCliente,
                            tokenFiscal,
                            framePrincipal.getCarrito().getProductos(), // Pasa la lista de compra
                            framePrincipal.getCarrito().getTotal()
                    );
                    hiloMail.start();
                }
            } catch (InterruptedException e) {
                System.out.println("[HILO 6] Error de concurrencia en barreras de sincronización.");
            }

            // 6. LIMPIAR EL CARRITO GLOBAL EN MEMORIA RAM PARA EL SIGUIENTE CLIENTE
            framePrincipal.getCarrito().getProductos().clear();

            // 7. Retornar al Login seguro en el EDT de Swing
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (framePrincipal.getLoginPanel() != null) {
                    framePrincipal.getLoginPanel().getCampoUsuario().setText("");
                    framePrincipal.getLoginPanel().getCampoContrasena().setText("");
                }

                // IMPORTANTE: Si el botón de pagar está en MisProductos, restablécelo a true antes de salir
                if (framePrincipal.getMisProductosPanel() != null) {
                    // Si creaste un getter para el botón o una función restablecer, invócala aquí
                    // framePrincipal.getMisProductosPanel().getBtnPagar().setEnabled(true);
                }

                framePrincipal.mostrarPanel(FrameP.LOGIN_PANEL);
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[HILO 6] Transacción interrumpida abruptamente.");
        }
    }
}
