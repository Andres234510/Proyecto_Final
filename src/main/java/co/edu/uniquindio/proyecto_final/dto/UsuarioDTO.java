package co.edu.uniquindio.proyecto_final.dto;

public class UsuarioDTO {
    private String id;
    private String nombre;
    private String correo;

    public UsuarioDTO() {}
    public UsuarioDTO(String id, String nombre, String correo) {
        this.id = id; this.nombre = nombre; this.correo = correo;
    }

    // getters/setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
}
