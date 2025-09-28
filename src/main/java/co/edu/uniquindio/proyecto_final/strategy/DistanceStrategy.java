package co.edu.uniquindio.proyecto_final.strategy;

import co.edu.uniquindio.proyecto_final.model.Envio;

public class DistanceStrategy implements TarifaStrategy {
    @Override
    public double calcular(Envio envio) {
        // simplificado: tarifa por "distancia estimada" simulada por longitud de ciudad string
        int origenLen = envio.getOrigen().getCiudad().length();
        int destinoLen = envio.getDestino().getCiudad().length();
        double distanciaSimulada = Math.abs(origenLen - destinoLen) + 10; // km simulados
        return 2000 + distanciaSimulada * 150; // tarifa base local
    }
}
