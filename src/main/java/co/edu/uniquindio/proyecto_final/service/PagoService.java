package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.repository.PagoRepository;

import java.util.List;

public class PagoService {

    private final PagoRepository pagoRepo;

    public PagoService(PagoRepository pagoRepo) {
        this.pagoRepo = pagoRepo;
    }

    public void crearPago(Pago p) {
        pagoRepo.save(p);
    }

    public void eliminarPago(String id) {
        pagoRepo.delete(id);
    }

    public void actualizarPago(Pago p) {
        pagoRepo.update(p);
    }

    public List<Pago> listarTodos() {
        return pagoRepo.findAll();
    }
}
