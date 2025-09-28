package co.edu.uniquindio.proyecto_final.model;

import java.util.UUID;

public class Repartidor {
    private String id;
    private String nombre;
    private boolean disponible = true;

    public Repartidor() { this.id = UUID.randomUUID().toString(); }
    public Repartidor(String nombre) { this(); this.nombre = nombre; }

    // getters y setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
