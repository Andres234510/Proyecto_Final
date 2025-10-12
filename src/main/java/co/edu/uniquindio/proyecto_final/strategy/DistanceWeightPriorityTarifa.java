package co.edu.uniquindio.proyecto_final.strategy;

import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.ServicioAdicional;

public class DistanceWeightPriorityTarifa implements TarifaCalculator {

    @Override
    public double calcular(Envio e) {
        if (e == null) return 0.0;

        double base = 5000.0;
        double costoPeso = e.getPeso() * 2000.0;
        double costoVolumen = e.getVolumen() * 100000.0;
        double costoServicios = 0.0;

        if (e.getServicios() != null) {
            if (e.getServicios().contains(ServicioAdicional.SEGURO)) costoServicios += 2000.0;
            if (e.getServicios().contains(ServicioAdicional.PRIORIDAD)) costoServicios += 5000.0;
            if (e.getServicios().contains(ServicioAdicional.FRAGIL)) costoServicios += 1500.0;
            if (e.getServicios().contains(ServicioAdicional.FIRMA_REQUERIDA)) costoServicios += 1000.0;
        }

        double total = base + costoPeso + costoVolumen + costoServicios;
        return Math.max(0.0, Math.round(total * 100.0) / 100.0);
    }
}
