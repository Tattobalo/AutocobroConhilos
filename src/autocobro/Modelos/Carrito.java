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

    public void agregarProducto(ProductoSeleccionado producto) {
        productos.add(producto);
        calcularTotal();
    }
    
    public void limpiarCarrito() {
        productos.clear();
        calcularTotal();
    }

    public List<ProductoSeleccionado> getProductos() {
        return productos;
    }

    private void calcularTotal() {
        total = 0.0;
        for (ProductoSeleccionado p : productos) {
            total += p.getSubtotal();
        }
    }
    
    public double getTotal() {
        return total;
    }
}