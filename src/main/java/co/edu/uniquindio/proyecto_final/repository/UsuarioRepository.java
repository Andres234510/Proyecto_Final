package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario u);
    Optional<Usuario> findById(String id);
    List<Usuario> findAll();
    void delete(String id);
    void update(Usuario usuario);
}
