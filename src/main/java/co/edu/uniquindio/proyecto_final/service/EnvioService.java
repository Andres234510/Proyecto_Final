package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Envio;

import java.util.List;
import java.util.Optional;

public interface EnvioService {
    Envio crear(Envio envio);
    Optional<Envio> buscar(String id);
    List<Envio> listarTodos();
    List<Envio> listarPorUsuario(String usuarioId);
    void asignarRepartidor(Envio envio);
}
