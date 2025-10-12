package co.edu.uniquindio.proyecto_final.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String password;      // nuevo campo
    private boolean admin = false;
    private List<Direccion> direcciones = new ArrayList<>();

    // Constructor viejo (compatibilidad)
    public Usuario(String nombre, String correo, String telefono) {
        this.idUsuario = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = ""; // por defecto vacío (puedes cambiar)
    }

    // Nuevo constructor que incluye password
    public Usuario(String nombre, String correo, String telefono, String password) {
        this.idUsuario = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
    }

    public Usuario(String idUsuario, String nombre, String correo, String telefono, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getPassword() { return password; }
    public List<Direccion> getDirecciones() { return direcciones; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setPassword(String password) { this.password = password; }
    public void setDirecciones(List<Direccion> direcciones) { this.direcciones = direcciones; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public String getId() { return idUsuario; }

    public String getIdDireccion() {
        if (direcciones == null || direcciones.isEmpty()) return null;
        return direcciones.get(0).getIdDireccion();
    }

    @Override
    public String toString() {
        return nombre + " <" + correo + ">";
    }
}
