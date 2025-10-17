package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.repository.DireccionRepository;

import java.util.List;

public class DireccionService {

    private final DireccionRepository direccionRepo;

    public DireccionService(DireccionRepository direccionRepo) {
        this.direccionRepo = direccionRepo;
    }

    public void crearDireccion(Direccion d) {
        direccionRepo.save(d);
    }

    public void eliminarDireccion(String id) {
        direccionRepo.delete(id);
    }

    public void actualizarDireccion(Direccion d) {
        direccionRepo.update(d);
    }

    public List<Direccion> listarTodos() {
        return direccionRepo.findAll();
    }
}
