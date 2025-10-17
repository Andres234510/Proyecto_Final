package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Disponibilidad;
import co.edu.uniquindio.proyecto_final.model.Repartidor;

import java.util.List;
import java.util.Optional;

public interface RepartidorService {
    Repartidor registrarRepartidor(Repartidor repartidor);
    Optional<Repartidor> buscarPorId(String id);
    List<Repartidor> listarTodos();
    void actualizarDisponibilidad(String id, boolean disponible);

    Repartidor crearRepartidor(String nombre, String documento, String telefono, Disponibilidad disponibilidad, String zona);


}
