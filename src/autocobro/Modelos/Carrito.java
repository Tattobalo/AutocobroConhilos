package autocobro.Modelos;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<ProductoSeleccionado> productos;
    private double total;

    public Carrito() {
        this.productos = new ArrayList<>();
        this.total = 0.0;
    }

    public synchronized void agregarProducto(ProductoSeleccionado producto) {
        productos.add(producto);
        calcularTotal();
    }
    
    public synchronized void limpiarCarrito() {
        productos.clear();
        calcularTotal();
    }

    public synchronized List<ProductoSeleccionado> getProductos() {
        return productos;
    }

    private synchronized void calcularTotal() {
        total = 0.0;
        for (ProductoSeleccionado p : productos) {
            total += p.getSubtotal();
        }
    }
    
    public synchronized double getTotal() {
        return total;
    }
    
    // Total redondeado a 2 decimales para UI y consola
    public synchronized double getTotalRedondeado() {
        return Math.round(total * 100.0) / 100.0;
    }
}