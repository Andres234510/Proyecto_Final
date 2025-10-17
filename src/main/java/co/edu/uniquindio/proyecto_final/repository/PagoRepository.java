package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Pago;

import java.util.List;
import java.util.Optional;

public interface PagoRepository {
    Pago save(Pago p);
    Optional<Pago> findById(String id);
    List<Pago> findAll();
    List<Pago> findByEnvioId(String idEnvio);
    void delete(String id);
    void update(Pago pago);
}
