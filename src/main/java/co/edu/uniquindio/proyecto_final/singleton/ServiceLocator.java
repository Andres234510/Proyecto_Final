package co.edu.uniquindio.proyecto_final.singleton;

import co.edu.uniquindio.proyecto_final.repository.*;
import co.edu.uniquindio.proyecto_final.service.*;
import co.edu.uniquindio.proyecto_final.strategy.DistanceStrategy;
import co.edu.uniquindio.proyecto_final.strategy.TarifaStrategy;

public class ServiceLocator {
    private static ServiceLocator instance;
    public final UsuarioRepository usuarioRepo;
    public final EnvioRepository envioRepo;
    public final RepartidorRepository repartidorRepo;

    public final UsuarioService usuarioService;
    public final EnvioService envioService;
    public final RepartidorService repartidorService;
    public final TarifaService tarifaService;

    private ServiceLocator() {
        this.usuarioRepo = new UsuarioRepositoryInMemory();
        this.envioRepo = new EnvioRepositoryInMemory();
        this.repartidorRepo = new RepartidorRepositoryInMemory();

        this.tarifaService = new TarifaService() {
            private TarifaStrategy strategy = new DistanceStrategy();
            @Override public double calcular(co.edu.uniquindio.proyecto_final.model.Envio envio) { return strategy.calcular(envio); }
            @Override public void setStrategy(TarifaStrategy s) { this.strategy = s; }
        };

        this.usuarioService = new co.edu.uniquindio.proyecto_final.service.UsuarioService() {
            @Override public co.edu.uniquindio.proyecto_final.model.Usuario registrar(String nombre, String correo, String password) {
                co.edu.uniquindio.proyecto_final.model.Usuario u = new co.edu.uniquindio.proyecto_final.model.Usuario(nombre, correo, password);
                usuarioRepo.save(u); return u;
            }
            @Override public java.util.Optional<co.edu.uniquindio.proyecto_final.model.Usuario> login(String correo, String password) {
                return usuarioRepo.findByCorreo(correo).filter(u -> u.getPassword().equals(password));
            }
            @Override public java.util.List<co.edu.uniquindio.proyecto_final.model.Usuario> listar() { return usuarioRepo.findAll(); }
        };

        this.repartidorService = new co.edu.uniquindio.proyecto_final.service.RepartidorServiceImpl(repartidorRepo);
        this.envioService = new co.edu.uniquindio.proyecto_final.service.EnvioServiceImpl(envioRepo, repartidorRepo, tarifaService);
    }

    public static ServiceLocator getInstance() {
        if (instance == null) instance = new ServiceLocator();
        return instance;
    }
}
