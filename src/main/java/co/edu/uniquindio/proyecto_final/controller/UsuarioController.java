package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Usuario crearUsuario(String nombre, String correo, String telefono) {
        Usuario u = new Usuario(nombre, correo, telefono);
        return repo.save(u);
    }

    public Optional<Usuario> buscarPorId(String id) {
        return repo.findById(id);
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public void eliminar(String id) {
        repo.delete(id);
    }
}
