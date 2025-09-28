package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Envio;

public interface TarifaService {
    double calcular(Envio envio);
    void setStrategy(co.edu.uniquindio.proyecto_final.strategy.TarifaStrategy s);
}
