package autocobro.Nucleo;


import autocobro.Modelos.Carrito;
import autocobro.Modelos.ProductoSeleccionado;


public class SeleccionDeProductosThread extends Thread {

    private final Carrito carrito;
    private volatile boolean activo = true;

    public SeleccionDeProductosThread(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public void run() {
        System.out.println("[HILO 3] Monitoreando cambios en el carrito...");

        while (activo) {
            try {
                Thread.sleep(5000);
                synchronized (carrito) {
                    for (ProductoSeleccionado p : carrito.getProductos()) {
                        System.out.println("[HILO 3] Producto: " + p.getNombre() + ", Cantidad: " + p.getCantidad());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("[HILO 3] Finalizado.");
    }

    public void detener() {
        activo = false;
    }
}
