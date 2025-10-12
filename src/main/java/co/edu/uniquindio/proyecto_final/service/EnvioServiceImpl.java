package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.*;
import co.edu.uniquindio.proyecto_final.repository.EnvioRepository;
import co.edu.uniquindio.proyecto_final.repository.RepartidorRepository;

import java.util.List;
import java.util.Optional;

public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepo;
    private final RepartidorRepository repartidorRepo;
    private final TarifaService tarifaService;

    public EnvioServiceImpl(EnvioRepository envioRepo,
                            RepartidorRepository repartidorRepo,
                            TarifaService tarifaService) {
        this.envioRepo = envioRepo;
        this.repartidorRepo = repartidorRepo;
        this.tarifaService = tarifaService;
    }

    @Override
    public Envio crear(Envio envio) {
        double costo = tarifaService.calcularCostoEstimado(envio);
        envio.setCosto(costo);
        envio.setEstado(EstadoEnvio.SOLICITADO);
        return envioRepo.save(envio);
    }

    public Envio actualizar(Envio envio) {
        double costo = tarifaService.calcularCostoEstimado(envio);
        envio.setCosto(costo);
        return envioRepo.save(envio);
    }

    public void eliminar(String idEnvio) {
        envioRepo.delete(idEnvio);
    }

    @Override
    public boolean asignarRepartidor(Envio envio) {
        List<Repartidor> disponibles = repartidorRepo.findByDisponibilidad(Disponibilidad.ACTIVO);
        if (disponibles == null || disponibles.isEmpty()) return false;

        Repartidor seleccionado = disponibles.get(0);
        seleccionado.setDisponibilidad(Disponibilidad.EN_RUTA);
        repartidorRepo.save(seleccionado);

        envio.setRepartidor(seleccionado);
        envio.setEstado(EstadoEnvio.ASIGNADO);
        envioRepo.save(envio);
        return true;
    }

    @Override
    public void actualizarEstado(String idEnvio, EstadoEnvio nuevoEstado) {
        envioRepo.findById(idEnvio).ifPresent(e -> { e.setEstado(nuevoEstado); envioRepo.save(e); });
    }

    @Override
    public List<Envio> listarPorEstado(EstadoEnvio estado) { return envioRepo.findByEstado(estado); }

    @Override
    public Optional<Envio> buscarPorId(String idEnvio) { return envioRepo.findById(idEnvio); }

    @Override
    public List<Envio> listarTodos() { return envioRepo.findAll(); }

    @Override
    public void cancelarEnvio(String idEnvio) { /* same as before */ envioRepo.findById(idEnvio).ifPresent(e -> { if (e.getEstado()==EstadoEnvio.SOLICITADO) { e.setEstado(EstadoEnvio.CANCELADO); envioRepo.save(e); } }); }

    @Override
    public void recalcularCosto(String idEnvio) { envioRepo.findById(idEnvio).ifPresent(envio -> { double nuevoCosto = tarifaService.calcularCostoEstimado(envio); envio.setCosto(nuevoCosto); envioRepo.save(envio); }); }

    @Override
    public List<Envio> listarPorUsuario(String usuarioId) { return envioRepo.findByUsuarioId(usuarioId); }
}
