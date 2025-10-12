package co.edu.uniquindio.proyecto_final.model;

import java.util.UUID;


public class Direccion {

    private String idDireccion;
    private String alias;
    private String calle;
    private String ciudad;
    private double lat;
    private double lon;

    public Direccion(String alias, String calle, String ciudad, double lat, double lon) {
        this.idDireccion = UUID.randomUUID().toString();
        this.alias = alias;
        this.calle = calle;
        this.ciudad = ciudad;
        this.lat = lat;
        this.lon = lon;
    }

    public Direccion(String alias, String calle, String ciudad) {
        this(alias, calle, ciudad, 0.0, 0.0);
    }

    public String getIdDireccion() { return idDireccion; }
    public String getAlias() { return alias; }
    public String getCalle() { return calle; }
    public String getCiudad() { return ciudad; }
    public double getLatitud() { return lat; }
    public double getLongitud() { return lon; }
    public String getId() { return idDireccion; }

    public void setAlias(String alias) { this.alias = alias; }
    public void setCalle(String calle) { this.calle = calle; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setLatitud(double lat) { this.lat = lat; }
    public void setLongitud(double lon) { this.lon = lon; }
}
