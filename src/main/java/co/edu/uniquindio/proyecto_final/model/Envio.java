package co.edu.uniquindio.proyecto_final.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Envio {
    private String id;
    private Direccion origen;
    private Direccion destino;
    private double peso;
    private double volumen;
    private boolean prioridad;
    private double costo;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private String usuarioId;

    public Envio() {
        this.id = UUID.randomUUID().toString();
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoEnvio.SOLICITADO;
    }

    // getters y setters
    public String getId() { return id; }
    public Direccion getOrigen() { return origen; }
    public void setOrigen(Direccion origen) { this.origen = origen; }
    public Direccion getDestino() { return destino; }
    public void setDestino(Direccion destino) { this.destino = destino; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getVolumen() { return volumen; }
    public void setVolumen(double volumen) { this.volumen = volumen; }
    public boolean isPrioridad() { return prioridad; }
    public void setPrioridad(boolean prioridad) { this.prioridad = prioridad; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
}
