package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.repository.RepartidorRepository;

import java.util.List;
import java.util.Optional;

public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repo;

    public RepartidorServiceImpl(RepartidorRepository repo) { this.repo = repo; }

    @Override
    public Repartidor registrar(String nombre) {
        Repartidor r = new Repartidor(nombre);
        return repo.save(r);
    }

    @Override
    public Optional<Repartidor> obtenerDisponible() {
        return repo.findAnyAvailable();
    }

    @Override
    public List<Repartidor> listar() {
        return repo.findAll();
    }
}
