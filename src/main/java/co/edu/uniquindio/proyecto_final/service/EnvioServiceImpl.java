package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.decorator.*;
import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.repository.EnvioRepository;
import co.edu.uniquindio.proyecto_final.repository.RepartidorRepository;

import java.util.List;
import java.util.Optional;

public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepo;
    private final RepartidorRepository repartidorRepo;
    private final TarifaService tarifaService;

    public EnvioServiceImpl(EnvioRepository envioRepo, RepartidorRepository repartidorRepo, TarifaService
            tarifaService) {
        this.envioRepo = envioRepo;
        this.repartidorRepo = repartidorRepo;
        this.tarifaService = tarifaService;
    }

    @Override
    public Envio crear(Envio envio) {
        // calcular tarifa por estrategia + decoradores si aplica
        double base = tarifaService.calcular(envio);
        // aplicar decoradores: ejemplo seguro + prioridad
        TarifaComponent tarifa = new BaseTarifa(base);
        if (envio.isPrioridad()) {
            tarifa = new PrioridadDecorator(tarifa);
        }
        // ejemplo: si volumen > 100 => seguro
        if (envio.getVolumen() > 100) {
            tarifa = new SeguroDecorator(tarifa);
        }
        envio.setCosto(tarifa.getCosto());
        envio = envioRepo.save(envio);
        return envio;
    }

    @Override
    public Optional<Envio> buscar(String id) {
        return envioRepo.findById(id);
    }

    @Override
    public List<Envio> listarTodos() {
        return envioRepo.findAll();
    }

    @Override
    public List<Envio> listarPorUsuario(String usuarioId) {
        return envioRepo.findByUsuarioId(usuarioId);
    }

    @Override
    public void asignarRepartidor(Envio envio) {
        Optional<Repartidor> r = repartidorRepo.findAnyAvailable();
        if (r.isPresent()) {
            Repartidor repartidor = r.get();
            repartidor.setDisponible(false);
            repartidorRepo.save(repartidor);
            envio.setEstado(co.edu.uniquindio.proyecto_final.model.EstadoEnvio.ASIGNADO);
            envioRepo.save(envio);
        }
    }
}
