package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Repartidor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepartidorRepositoryInMemory implements RepartidorRepository {
    private final List<Repartidor> repartidores = new ArrayList<>();

    @Override
    public Repartidor save(Repartidor r) {
        repartidores.removeIf(x -> x.getId().equals(r.getId()));
        repartidores.add(r);
        return r;
    }

    @Override
    public Optional<Repartidor> findAnyAvailable() {
        return repartidores.stream().filter(Repartidor::isDisponible).findFirst();
    }

    @Override
    public List<Repartidor> findAll() {
        return new ArrayList<>(repartidores);
    }
}
