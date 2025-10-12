package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Disponibilidad;
import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.repository.RepartidorRepository;

import java.util.List;
import java.util.Optional;

public class RepartidorServiceImpl implements RepartidorService {

    private final RepartidorRepository repo;

    public RepartidorServiceImpl(RepartidorRepository repo) {
        this.repo = repo;
    }

    @Override
    public Repartidor registrarRepartidor(Repartidor repartidor) { return repo.save(repartidor); }

    @Override
    public Optional<Repartidor> buscarPorId(String id) { return repo.findById(id); }

    @Override
    public List<Repartidor> listarTodos() { return repo.findAll(); }

    @Override
    public void actualizarDisponibilidad(String id, boolean disponible) {
        repo.findById(id).ifPresent(r -> {
            r.setDisponibilidad(disponible ? Disponibilidad.ACTIVO : Disponibilidad.INACTIVO);
            repo.save(r);
        });
    }

    @Override
    public Repartidor crearRepartidor(String nombre, String documento, String telefono, Disponibilidad disponibilidad, String zona) {
        Repartidor r = new Repartidor(nombre, documento, telefono, disponibilidad, zona);
        return repo.save(r);
    }

    public Repartidor actualizarRepartidor(Repartidor r) { return repo.save(r); }

    public void eliminarRepartidor(String id) { repo.delete(id); }
}
