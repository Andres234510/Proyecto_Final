package co.edu.uniquindio.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private String password;
    private List<Direccion> direcciones = new ArrayList<>();
    private boolean admin = false;

    public Usuario() { this.id = UUID.randomUUID().toString(); }

    public Usuario(String nombre, String correo, String password) {
        this();
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    // getters y setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Direccion> getDirecciones() { return direcciones; }
    public void setDirecciones(List<Direccion> direcciones) { this.direcciones = direcciones; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
