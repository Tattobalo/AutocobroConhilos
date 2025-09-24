package autocobro.Nucleo;

import autocobro.Modelos.Carrito;

public class CalculaPrecioThread extends Thread {

    private final Carrito carrito;
    private volatile boolean activo = true;

    public CalculaPrecioThread(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(5000);
                System.out.println("[HILO 4] Total actual: $" + carrito.getTotal());
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
