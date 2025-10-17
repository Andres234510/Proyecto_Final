package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Direccion;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository {
    Direccion save(Direccion d);
    Optional<Direccion> findById(String id);
    List<Direccion> findAll();
    List<Direccion> findByAlias(String alias);
    void delete(String id);
    void update(Direccion d);
}
