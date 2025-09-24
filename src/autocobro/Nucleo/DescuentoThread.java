package autocobro.Nucleo;

import autocobro.Modelos.Carrito;
import autocobro.Modelos.ProductoSeleccionado;

public class DescuentoThread extends Thread {
    private final Carrito carrito;

    public DescuentoThread(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public void run() {
        System.out.println("[HILO 6] Aplicando descuento...");

        synchronized (carrito) {
            for (ProductoSeleccionado p : carrito.getProductos()) {
                if (p.getCantidad() >= 3) { // ejemplo: solo si cantidad >= 3
                    double descuento = p.getPrecio() * 0.10; // 10%
                    p.setPrecio(p.getPrecio() - descuento);
                    System.out.println("[HILO 6] " + p.getNombre() + " | Precio con descuento: $" + p.getPrecio());
                }
            }
        }

        System.out.println("[HILO 6] Descuento aplicado.");
    }
}
