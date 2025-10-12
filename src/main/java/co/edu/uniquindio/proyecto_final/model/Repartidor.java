package co.edu.uniquindio.proyecto_final.model;

import java.util.UUID;

/**
 * Repartidor con compatibilidad de métodos:
 * - getIdRepartidor(), getZona(), setDisponibilidad(...)
 * - constructores con distintos parámetros para compatibilidad
 */
public class Repartidor {
    private String idRepartidor;
    private String nombre;
    private String documento;
    private String telefono;
    private Disponibilidad disponibilidad;
    private String zona;

    public Repartidor(String nombre, String documento, String telefono, Disponibilidad disponibilidad, String zona) {
        this.idRepartidor = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.disponibilidad = disponibilidad != null ? disponibilidad : Disponibilidad.INACTIVO;
        this.zona = zona;
    }

    public Repartidor(String nombre) {
        this(nombre, "", "", Disponibilidad.INACTIVO, "Sin zona");
    }

    public String getIdRepartidor() { return idRepartidor; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getTelefono() { return telefono; }
    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public String getZona() { return zona; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDocumento(String documento) { this.documento = documento; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }
    public void setZona(String zona) { this.zona = zona; }

    public String getId() { return idRepartidor; }
}
