package co.edu.uniquindio.proyecto_final.singleton;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.model.Usuario;

public class DataInitializer {
    public static void init() {
        ServiceLocator sl = ServiceLocator.getInstance();
        // crear usuarios demo
        Usuario admin = new Usuario("Admin", "admin@uniquindio.edu.co", "admin");
        admin.setAdmin(true);
        sl.usuarioRepo.save(admin);

        Usuario u1 = new Usuario("Andres", "andres@example.com", "1234");
        u1.getDirecciones().add(new Direccion("Casa", "Calle 1 #1-1", "Armenia"));
        u1.getDirecciones().add(new Direccion("Trabajo", "Av. Centro #10", "Pereira"));
        sl.usuarioRepo.save(u1);

        // repartidores demo
        sl.repartidorRepo.save(new Repartidor("Luis"));
        sl.repartidorRepo.save(new Repartidor("Ana"));
    }
}
