package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.EstadoEnvio;

import java.util.List;
import java.util.Optional;

public interface EnvioService {

    Envio crear(Envio envio);

    default Envio crearEnvio(Envio envio) {
        return crear(envio);
    }

    boolean asignarRepartidor(Envio envio);

    void actualizarEstado(String idEnvio, EstadoEnvio nuevoEstado);

    List<Envio> listarPorEstado(EstadoEnvio estado);

    Optional<Envio> buscarPorId(String idEnvio);

    List<Envio> listarTodos();

    void cancelarEnvio(String idEnvio);

    void recalcularCosto(String idEnvio);

    List<Envio> listarPorUsuario(String usuarioId);
}
