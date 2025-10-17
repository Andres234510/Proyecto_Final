package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.model.Disponibilidad;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepartidorRepository implements RepartidorRepository {
    private final Map<String, Repartidor> storage = new HashMap<>();
    private final List<Repartidor> repartidores = new ArrayList<>();

    @Override
    public Repartidor save(Repartidor r) {
        storage.put(r.getIdRepartidor(), r);
        return r;
    }

    @Override
    public Optional<Repartidor> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Repartidor> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Repartidor> findByZona(String zona) {
        return storage.values().stream()
                .filter(r -> r.getZona() != null && r.getZona().equalsIgnoreCase(zona))
                .collect(Collectors.toList());
    }

    @Override
    public List<Repartidor> findByDisponibilidad(Disponibilidad d) {
        return storage.values().stream()
                .filter(r -> r.getDisponibilidad() == d)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }

    @Override
    public void update(Repartidor repartidor) {
        delete(repartidor.getId());
        repartidores.add(repartidor);
    }
}
