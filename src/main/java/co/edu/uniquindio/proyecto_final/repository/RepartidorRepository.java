package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.model.Disponibilidad;

import java.util.List;
import java.util.Optional;

public interface RepartidorRepository {
    Repartidor save(Repartidor r);
    Optional<Repartidor> findById(String id);
    List<Repartidor> findAll();
    List<Repartidor> findByZona(String zona);
    List<Repartidor> findByDisponibilidad(Disponibilidad d);
    void delete(String id);
}
