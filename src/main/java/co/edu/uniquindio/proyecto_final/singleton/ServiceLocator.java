package co.edu.uniquindio.proyecto_final.singleton;

import co.edu.uniquindio.proyecto_final.repository.*;
import co.edu.uniquindio.proyecto_final.service.*;
import co.edu.uniquindio.proyecto_final.strategy.DistanceWeightPriorityTarifa;
import co.edu.uniquindio.proyecto_final.strategy.TarifaCalculator;

/**
 * ServiceLocator con campos públicos (compatibilidad con código que accede directamente
 * a ServiceLocator.usuarioService / .envioService / repos).
 *
 * Reemplaza tu ServiceLocator anterior por este archivo.
 */
public class ServiceLocator {

    private static ServiceLocator instance;

    // Repos (públicos para compatibilidad)
    public final UsuarioRepository usuarioRepo;
    public final EnvioRepository envioRepo;
    public final RepartidorRepository repartidorRepo;
    public final DireccionRepository direccionRepo;
    public final PagoRepository pagoRepo;

    // Servicios (públicos para compatibilidad con acceso directo)
    public final UsuarioService usuarioService;
    public final EnvioService envioService;
    public final RepartidorService repartidorService;
    public final TarifaService tarifaService;
    public final PagoService pagoService;

    private ServiceLocator() {
        // repos in-memory
        this.usuarioRepo = new InMemoryUsuarioRepository();
        this.envioRepo = new InMemoryEnvioRepository();
        this.repartidorRepo = new InMemoryRepartidorRepository();
        this.direccionRepo = new InMemoryDireccionRepository();
        this.pagoRepo = new InMemoryPagoRepository();

        // estrategia de tarifa
        TarifaCalculator calc = new DistanceWeightPriorityTarifa();
        this.tarifaService = new TarifaService(calc);

        // servicios (implementaciones)
        this.usuarioService = new UsuarioService(usuarioRepo, direccionRepo);
        this.repartidorService = new RepartidorServiceImpl(repartidorRepo);
        this.envioService = new EnvioServiceImpl(envioRepo, repartidorRepo, tarifaService);
        this.pagoService = new PagoService(pagoRepo);
    }

    public static synchronized ServiceLocator getInstance() {
        if (instance == null) instance = new ServiceLocator();
        return instance;
    }
}
