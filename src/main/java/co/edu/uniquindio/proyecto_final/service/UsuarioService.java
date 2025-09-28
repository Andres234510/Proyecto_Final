package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrar(String nombre, String correo, String password);
    Optional<Usuario> login(String correo, String password);
    List<Usuario> listar();
}
