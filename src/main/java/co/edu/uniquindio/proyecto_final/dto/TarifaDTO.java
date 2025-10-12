package co.edu.uniquindio.proyecto_final.dto;

public class TarifaDTO {
    public String idEnvio;
    public double costoBase;
    public double costoPeso;
    public double costoVolumen;
    public double costoServicios;
    public double costoTotal;

    public TarifaDTO() {}

    public TarifaDTO(String idEnvio, double costoBase, double costoPeso, double costoVolumen, double costoServicios) {
        this.idEnvio = idEnvio;
        this.costoBase = costoBase;
        this.costoPeso = costoPeso;
        this.costoVolumen = costoVolumen;
        this.costoServicios = costoServicios;
        this.costoTotal = costoBase + costoPeso + costoVolumen + costoServicios;
    }
}
