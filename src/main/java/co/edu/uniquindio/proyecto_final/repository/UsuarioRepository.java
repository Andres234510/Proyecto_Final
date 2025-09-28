package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findById(String id);
    List<Usuario> findAll();
}
