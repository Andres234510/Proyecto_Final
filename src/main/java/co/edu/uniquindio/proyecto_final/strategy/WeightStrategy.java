package co.edu.uniquindio.proyecto_final.strategy;

import co.edu.uniquindio.proyecto_final.model.Envio;

public class WeightStrategy implements TarifaStrategy {
    @Override
    public double calcular(Envio envio) {
        return 1000 + envio.getPeso() * 800;
    }
}
