package autocobro.Modelos;

public class Usuarios {
    private int id; // ID del usuario
    private String nombreUsuario;
    private String correo;
    private String rutaFotoPerfil;

    public Usuarios(int id, String nombreUsuario, String correo,String rutaFotoPerfil) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.rutaFotoPerfil = rutaFotoPerfil;
    }

    public int getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getCorreo(){return correo; }
    public String getRutaFotoPerfil() { return rutaFotoPerfil; }
}
