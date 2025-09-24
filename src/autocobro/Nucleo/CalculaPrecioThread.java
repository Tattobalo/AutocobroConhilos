package autocobro.Nucleo;

import autocobro.Modelos.Carrito;
import autocobro.UI.FrameP;
import javax.swing.SwingUtilities;

public class CalculaPrecioThread extends Thread {

    private final Carrito carrito;
    private volatile boolean activo = true;
    private final FrameP framePrincipal;

    public CalculaPrecioThread(Carrito carrito, FrameP framePrincipal) {
        this.carrito = carrito;
        this.framePrincipal = framePrincipal;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(5000);

                SwingUtilities.invokeLater(() -> {
                    framePrincipal.getMisProductosPanel().actualizarPrecios();
                });

                System.out.println("[HILO 4] Total actual: $" + carrito.getTotalRedondeado());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("[Hilo 4] Finalizado");
    }

    public void detener() {
        activo = false;
    }

}
