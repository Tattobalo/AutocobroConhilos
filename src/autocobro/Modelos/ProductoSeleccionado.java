package autocobro.Modelos;

public class ProductoSeleccionado {
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;

    public ProductoSeleccionado(String nombre, String descripcion, double precio, int cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public double getSubtotal() {
        return precio * cantidad;
    }
}