package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.repository.PagoRepository;

import java.util.List;
import java.util.Optional;

public class PagoService {
    private final PagoRepository repo;

    public PagoService(PagoRepository repo) {
        this.repo = repo;
    }

    public Pago crearPago(Pago pago) {
        return repo.save(pago);
    }

    public Optional<Pago> buscarPorId(String id) {
        return repo.findById(id);
    }

    public List<Pago> listarTodos() {
        return repo.findAll();
    }

    public List<Pago> listarPorEnvio(String idEnvio) {
        return repo.findByEnvioId(idEnvio);
    }

    public void eliminarPago(String id) {
        repo.delete(id);
    }
}
