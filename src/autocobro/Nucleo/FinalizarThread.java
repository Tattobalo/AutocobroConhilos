package autocobro.Nucleo;

import autocobro.UI.FrameP;

public class FinalizarThread extends Thread {

    private final FrameP framePrincipal;

    public FinalizarThread(FrameP framePrincipal) {
        this.framePrincipal = framePrincipal;
    }

    @Override
    public void run() {
        System.out.println("[HILO 6] Finalizando compra...");

        try {
            // Simular procesamiento de pago
            Thread.sleep(2000);

            // Detener hilos de carrito
            framePrincipal.detenerHilosCarrito();

            // Detener hilo de sesión
            framePrincipal.cerrarSesion();

            // Limpiar carrito
            framePrincipal.getCarrito().getProductos().clear();

            //Iniciar hilo 7
            GenerarTicketThread hilo7 = new GenerarTicketThread(framePrincipal.getCarrito(), framePrincipal.getUsuarioActual());
            hilo7.start();

            System.out.println("[HILO 6] Compra finalizada correctamente.");

            // Volver al login en la UI (en el hilo de eventos de Swing)
            javax.swing.SwingUtilities.invokeLater(() -> framePrincipal.mostrarPanel(FrameP.LOGIN_PANEL));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[HILO 6] Proceso de finalización interrumpido.");
        }
    }
}
