package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.EstadoEnvio;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryEnvioRepository implements EnvioRepository {
    private final Map<String, Envio> storage = new HashMap<>();

    @Override
    public Envio save(Envio envio) {
        storage.put(envio.getIdEnvio(), envio);
        return envio;
    }

    @Override
    public Optional<Envio> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Envio> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Envio> findByEstado(EstadoEnvio estado) {
        return storage.values().stream()
                .filter(e -> e.getEstado() == estado)
                .collect(Collectors.toList());
    }

    @Override
    public List<Envio> findByUsuarioId(String usuarioId) {
        return storage.values().stream()
                .filter(e -> e.getUsuario() != null && usuarioId.equals(e.getUsuario().getIdUsuario()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
