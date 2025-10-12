package co.edu.uniquindio.proyecto_final.dto;

import java.time.LocalDateTime;

public class PagoDTO {
    public String idPago;
    public String idEnvio;
    public double monto;
    public String metodo;
    public String resultado;
    public LocalDateTime fecha;

    public PagoDTO() {}
}
