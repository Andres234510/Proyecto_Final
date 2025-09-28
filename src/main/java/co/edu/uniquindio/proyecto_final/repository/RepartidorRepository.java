package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Repartidor;

import java.util.List;
import java.util.Optional;

public interface RepartidorRepository {
    Repartidor save(Repartidor r);
    Optional<Repartidor> findAnyAvailable();
    List<Repartidor> findAll();
}
