package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioRepositoryInMemory implements UsuarioRepository {
    private final List<Usuario> usuarios = new ArrayList<>();

    @Override
    public Usuario save(Usuario usuario) {
        usuarios.removeIf(u -> u.getId().equals(usuario.getId()));
        usuarios.add(usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarios.stream().filter(u -> u.getCorreo().equalsIgnoreCase(correo)).findFirst();
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }
}
