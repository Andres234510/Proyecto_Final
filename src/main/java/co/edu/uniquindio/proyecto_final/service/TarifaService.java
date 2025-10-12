package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.ServicioAdicional;
import co.edu.uniquindio.proyecto_final.strategy.TarifaCalculator;

public class TarifaService {

    private final TarifaCalculator calculator;

    public TarifaService(TarifaCalculator calculator) {
        this.calculator = calculator;
    }

    public double calcularCostoEstimado(Envio envio) {
        double costo = calculator.calcular(envio);

        if (envio.getServicios().contains(ServicioAdicional.PRIORIDAD)) {
            costo *= 1.3;
        }
        if (envio.getServicios().contains(ServicioAdicional.SEGURO)) {
            costo += 5000;
        }

        return costo;
    }
}
