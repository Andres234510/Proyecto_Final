package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Direccion;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryDireccionRepository implements DireccionRepository {
    private final Map<String, Direccion> storage = new HashMap<>();

    @Override
    public Direccion save(Direccion d) {
        storage.put(d.getIdDireccion(), d);
        return d;
    }

    @Override
    public Optional<Direccion> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Direccion> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Direccion> findByAlias(String alias) {
        return storage.values().stream()
                .filter(d -> d.getAlias() != null && d.getAlias().equalsIgnoreCase(alias))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
