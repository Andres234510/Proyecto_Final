package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Envio;

import java.util.List;
import java.util.Optional;

public interface EnvioRepository {
    Envio save(Envio envio);
    Optional<Envio> findById(String id);
    List<Envio> findAll();
    List<Envio> findByUsuarioId(String usuarioId);
}
