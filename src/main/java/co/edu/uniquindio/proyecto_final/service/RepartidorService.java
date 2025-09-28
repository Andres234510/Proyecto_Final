package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Repartidor;

import java.util.List;
import java.util.Optional;

public interface RepartidorService {
    Repartidor registrar(String nombre);
    Optional<Repartidor> obtenerDisponible();
    List<Repartidor> listar();
}
