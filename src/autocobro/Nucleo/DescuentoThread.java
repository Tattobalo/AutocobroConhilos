package autocobro.Nucleo;

import autocobro.Modelos.Carrito;
import autocobro.Modelos.ProductoSeleccionado;

public class DescuentoThread extends Thread {
    private final Carrito carrito;
    private volatile boolean activo = true;

    public DescuentoThread(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public void run() {
        System.out.println("[HILO 6] Iniciado - Verificando promociones y aplicando descuentos...");

        while (activo) {
            try {
                Thread.sleep(3000); // cada 3 segundos revisa descuentos
                synchronized (carrito) {
                    for (ProductoSeleccionado p : carrito.getProductos()) {
                        if (p.getCantidad() >= 3) {
                            double precioOriginal = p.getPrecio();
                            double descuento = precioOriginal * 0.10; // 10% de descuento
                            double nuevoPrecio = precioOriginal - descuento;
                            System.out.println("[HILO 6] Aplicando 10% de descuento a " + p.getNombre() +
                                               " -> Nuevo precio: $" + nuevoPrecio);
                            p.setPrecio(nuevoPrecio); // requiere setter en ProductoSeleccionado
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("[HILO 6] Finalizado.");
    }

    public void detener() {
        activo = false;
    }
}

