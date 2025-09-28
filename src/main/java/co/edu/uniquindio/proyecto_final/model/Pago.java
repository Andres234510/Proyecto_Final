package co.edu.uniquindio.proyecto_final.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pago {
    private String id;
    private String envioId;
    private double valor;
    private LocalDateTime fecha;

    public Pago() { this.id = UUID.randomUUID().toString(); this.fecha = LocalDateTime.now(); }

    // getters/setters
    public String getId() { return id; }
    public String getEnvioId() { return envioId; }
    public void setEnvioId(String envioId) { this.envioId = envioId; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getFecha() { return fecha; }
}
