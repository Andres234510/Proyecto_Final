package co.edu.uniquindio.proyecto_final.strategy;

import co.edu.uniquindio.proyecto_final.model.Envio;

public interface TarifaStrategy {
    double calcular(Envio envio);
}
