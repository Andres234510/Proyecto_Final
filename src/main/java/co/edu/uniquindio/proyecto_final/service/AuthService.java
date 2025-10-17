package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import java.util.Optional;


public class AuthService {

    private final UsuarioService usuarioService;

    public AuthService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    public Optional<Usuario> login(String correo, String contrasenia) {
        if (correo == null || contrasenia == null) return Optional.empty();
        return usuarioService.login(correo, contrasenia);
    }
}
