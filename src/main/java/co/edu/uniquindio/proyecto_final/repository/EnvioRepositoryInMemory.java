package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Envio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnvioRepositoryInMemory implements EnvioRepository {
    private final List<Envio> envios = new ArrayList<>();

    @Override
    public Envio save(Envio envio) {
        envios.removeIf(e -> e.getId().equals(envio.getId()));
        envios.add(envio);
        return envio;
    }

    @Override
    public Optional<Envio> findById(String id) {
        return envios.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Envio> findAll() {
        return new ArrayList<>(envios);
    }

    @Override
    public List<Envio> findByUsuarioId(String usuarioId) {
        return envios.stream().filter(e -> usuarioId.equals(e.getUsuarioId())).collect(Collectors.toList());
    }
}
