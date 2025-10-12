package co.edu.uniquindio.proyecto_final.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pago {
    private String idPago;
    private String idEnvio;
    private double monto;
    private LocalDateTime fecha;
    private String metodo;
    private ResultadoPago resultado;

    public Pago(String idEnvio, double monto, String metodo, ResultadoPago resultado) {
        this.idPago = UUID.randomUUID().toString();
        this.idEnvio = idEnvio;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.metodo = metodo;
        this.resultado = resultado;
    }

    public String getIdPago() { return idPago; }
    public String getIdEnvio() { return idEnvio; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public String getMetodo() { return metodo; }
    public ResultadoPago getResultado() { return resultado; }
}
