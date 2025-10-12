package co.edu.uniquindio.proyecto_final.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Envio {
    private String idEnvio;
    private Direccion origen;
    private Direccion destino;
    private double peso;
    private double volumen;
    private double costo;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEstimadaEntrega;
    private Repartidor repartidor;
    private Usuario usuario;
    private List<ServicioAdicional> servicios = new ArrayList<>();

    public Envio() {
        this.idEnvio = UUID.randomUUID().toString();
        this.estado = EstadoEnvio.SOLICITADO;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Envio(String idEnvio, Usuario usuario, Direccion origen, Direccion destino, double peso) {
        this();
        if (idEnvio != null) this.idEnvio = idEnvio;
        this.usuario = usuario;
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public String getIdEnvio() { return idEnvio; }
    public Direccion getOrigen() { return origen; }
    public void setOrigen(Direccion origen) { this.origen = origen; }
    public Direccion getDestino() { return destino; }
    public void setDestino(Direccion destino) { this.destino = destino; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public double getVolumen() { return volumen; }
    public void setVolumen(double volumen) { this.volumen = volumen; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) { this.fechaEstimadaEntrega = fechaEstimadaEntrega; }
    public Repartidor getRepartidor() { return repartidor; }
    public void setRepartidor(Repartidor repartidor) { this.repartidor = repartidor; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<ServicioAdicional> getServicios() { return servicios; }

    public void addServicio(ServicioAdicional s) {
        if (s != null && !servicios.contains(s)) servicios.add(s);
    }

    public void setUsuarioId(String usuarioId) {
        if (usuarioId == null) return;
        this.usuario = new Usuario(usuarioId, "", "");
    }

    public void setIdEnvio(String id) { this.idEnvio = id; }
    public void setId(String id) { this.idEnvio = id; }
}
