package co.edu.uniquindio.proyecto_final.singleton;

import co.edu.uniquindio.proyecto_final.model.*;
import co.edu.uniquindio.proyecto_final.repository.*;
import co.edu.uniquindio.proyecto_final.service.*;
import co.edu.uniquindio.proyecto_final.strategy.DistanceWeightPriorityTarifa;
import co.edu.uniquindio.proyecto_final.strategy.TarifaCalculator;

import java.time.LocalDateTime;

public class DataStore {
    private static DataStore instance;

    private final UsuarioRepository usuarioRepo;
    private final RepartidorRepository repartidorRepo;
    private final EnvioRepository envioRepo;
    private final PagoRepository pagoRepo;
    private final DireccionRepository direccionRepo;
    private final UsuarioService usuarioService;
    private final RepartidorServiceImpl repartidorService;
    private final EnvioServiceImpl envioService;
    private final PagoService pagoService;
    private final TarifaService tarifaService;
    private final DireccionService direccionService;

    private DataStore() {
        this.usuarioRepo = new InMemoryUsuarioRepository();
        this.repartidorRepo = new InMemoryRepartidorRepository();
        this.envioRepo = new InMemoryEnvioRepository();
        this.pagoRepo = new InMemoryPagoRepository();
        this.direccionRepo = new InMemoryDireccionRepository();

        TarifaCalculator calc = new DistanceWeightPriorityTarifa();
        this.tarifaService = new TarifaService(calc);

        this.usuarioService = new UsuarioService(usuarioRepo, direccionRepo);
        this.repartidorService = new RepartidorServiceImpl(repartidorRepo);
        this.envioService = new EnvioServiceImpl(envioRepo, repartidorRepo, tarifaService);
        this.pagoService = new PagoService(pagoRepo);
        this.direccionService = new DireccionService(direccionRepo);
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public UsuarioService getUsuarioService() { return usuarioService; }
    public RepartidorServiceImpl getRepartidorService() { return repartidorService; }
    public EnvioServiceImpl getEnvioService() { return envioService; }
    public PagoService getPagoService() { return pagoService; }
    public TarifaService getTarifaService() { return tarifaService; }
    public DireccionService getDireccionService() { return direccionService; }
    public UsuarioRepository getUsuarioRepo() { return usuarioRepo; }
    public RepartidorRepository getRepartidorRepo() { return repartidorRepo; }
    public EnvioRepository getEnvioRepo() { return envioRepo; }
    public PagoRepository getPagoRepo() { return pagoRepo; }
    public DireccionRepository getDireccionRepo() { return direccionRepo; }
    public void initSampleData() {
        if (usuarioRepo.findAll().isEmpty()) {
            Usuario u1 = usuarioService.crearUsuario("Ana Perez", "ana@example.com", "3101111111");
            usuarioService.agregarDireccionAUsuario(u1.getIdUsuario(), new Direccion("Casa", "Cl 10 #5-20", "Bogota", 4.6, -74.07));
            Usuario u2 = usuarioService.crearUsuario("Luis Gomez", "luis@example.com", "3102222222");
            usuarioService.agregarDireccionAUsuario(u2.getIdUsuario(), new Direccion("Oficina", "Av 9 #34-56", "Bogota", 4.65, -74.05));

            repartidorService.crearRepartidor("Carlos", "1001", "3201111111", Disponibilidad.ACTIVO, "Norte");
            repartidorService.crearRepartidor("Maria", "1002", "3202222222", Disponibilidad.ACTIVO, "Sur");

            Envio e1 = new Envio();
            e1.setUsuario(u1);
            e1.setOrigen(u1.getDirecciones().get(0));
            e1.setDestino(u2.getDirecciones().get(0));
            e1.setPeso(2.0);
            e1.setVolumen(0.01);
            envioService.crearEnvio(e1);

            Pago p1 = new Pago(e1.getIdEnvio(), e1.getCosto(), "TarjetaSimulada", ResultadoPago.APROBADO);
            pagoService.crearPago(p1);
        }
    }
}
