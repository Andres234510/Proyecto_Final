package co.edu.uniquindio.proyecto_final.model;

import java.util.UUID;

public class Direccion {
    private String id;
    private String alias;
    private String calle;
    private String ciudad;

    public Direccion() { this.id = UUID.randomUUID().toString(); }

    public Direccion(String alias, String calle, String ciudad) {
        this();
        this.alias = alias;
        this.calle = calle;
        this.ciudad = ciudad;
    }

    // getters y setters
    public String getId() { return id; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    @Override
    public String toString() {
        return alias + " - " + calle + ", " + ciudad;
    }
}
