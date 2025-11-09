package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Disponibilidad;
import co.edu.uniquindio.proyecto_final.model.Repartidor;

import java.util.List;
import java.util.Optional;

public interface RepartidorService {

    // Registra un nuevo repartidor en el sistema
    Repartidor registrarRepartidor(Repartidor repartidor);

    // Busca un repartidor por su ID
    Optional<Repartidor> buscarPorId(String id);

    // Lista todos los repartidores registrados
    List<Repartidor> listarTodos();

    // Actualiza la disponibilidad de un repartidor por ID
    void actualizarDisponibilidad(String id, boolean disponible);

    // Crea un nuevo repartidor con los datos básicos
    Repartidor crearRepartidor(String nombre, String documento,
                               String telefono, Disponibilidad disponibilidad, String zona);

    // --- ✅ Métodos agregados para compatibilidad con RepartidorTab ---
    // Actualiza los datos de un repartidor existente
    Repartidor actualizarRepartidor(Repartidor repartidor);

    // Elimina un repartidor por su ID
    void eliminarRepartidor(String id);
}
