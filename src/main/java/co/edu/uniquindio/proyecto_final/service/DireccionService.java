package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.repository.DireccionRepository;

import java.util.List;
import java.util.Optional;

public class DireccionService {

    private final DireccionRepository repo;

    public DireccionService(DireccionRepository repo) {
        this.repo = repo;
    }

    public Direccion crearDireccion(Direccion d) {
        return repo.save(d);
    }

    public Optional<Direccion> buscarPorId(String id) { return repo.findById(id); }

    public List<Direccion> listarTodos() { return repo.findAll(); }

    public Direccion actualizarDireccion(Direccion d) { return repo.save(d); }

    public void eliminarDireccion(String id) { repo.delete(id); }
}
