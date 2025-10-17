package co.edu.uniquindio.proyecto_final.singleton;

import co.edu.uniquindio.proyecto_final.repository.*;
import co.edu.uniquindio.proyecto_final.service.*;
import co.edu.uniquindio.proyecto_final.strategy.DistanceWeightPriorityTarifa;
import co.edu.uniquindio.proyecto_final.strategy.TarifaCalculator;


public class ServiceLocator {

    private static ServiceLocator instance;

    public final UsuarioRepository usuarioRepo;
    public final EnvioRepository envioRepo;
    public final RepartidorRepository repartidorRepo;
    public final DireccionRepository direccionRepo;
    public final PagoRepository pagoRepo;

    public final UsuarioService usuarioService;
    public final EnvioService envioService;
    public final RepartidorService repartidorService;
    public final TarifaService tarifaService;
    public final PagoService pagoService;

    private ServiceLocator() {
        this.usuarioRepo = new InMemoryUsuarioRepository();
        this.envioRepo = new InMemoryEnvioRepository();
        this.repartidorRepo = new InMemoryRepartidorRepository();
        this.direccionRepo = new InMemoryDireccionRepository();
        this.pagoRepo = new InMemoryPagoRepository();

        TarifaCalculator calc = new DistanceWeightPriorityTarifa();
        this.tarifaService = new TarifaService(calc);

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
