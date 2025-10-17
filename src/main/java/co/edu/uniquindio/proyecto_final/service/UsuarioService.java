package co.edu.uniquindio.proyecto_final.service;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.repository.DireccionRepository;
import co.edu.uniquindio.proyecto_final.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;


public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final DireccionRepository direccionRepo;

    public UsuarioService(UsuarioRepository usuarioRepo, DireccionRepository direccionRepo) {
        this.usuarioRepo = usuarioRepo;
        this.direccionRepo = direccionRepo;
    }

    public Usuario crearUsuario(String nombre, String correo, String telefono, String password) {
        Usuario u = new Usuario(nombre, correo, telefono, password);
        return usuarioRepo.save(u);
    }

    public Usuario actualizarUsuario(Usuario u) {
        return usuarioRepo.save(u);
    }

    public void eliminarUsuario(String id) {
        usuarioRepo.delete(id);
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepo.findById(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepo.findAll();
    }

    public List<Usuario> listarUsuarios() { return listarTodos(); }

    public Optional<Usuario> login(String correo, String contrasenia) {
        return usuarioRepo.findAll().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo)
                        && u.getPassword() != null
                        && u.getPassword().equals(contrasenia))
                .findFirst();
    }

    public Usuario loginRaw(String correo, String contrasenia) {
        return login(correo, contrasenia).orElse(null);
    }

    public Usuario registrar(String nombre, String correo, String contrasenia) {
        Optional<Usuario> existe = usuarioRepo.findAll().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
        if (existe.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo: " + correo);
        }

        Usuario u = new Usuario(nombre, correo, "", contrasenia);
        return usuarioRepo.save(u);
    }

    public Direccion agregarDireccionAUsuario(String usuarioId, Direccion direccion) {
        Optional<Usuario> opt = usuarioRepo.findById(usuarioId);
        if (opt.isEmpty()) throw new IllegalArgumentException("Usuario no existe: " + usuarioId);
        Direccion dSaved = direccionRepo.save(direccion);
        Usuario u = opt.get();
        u.getDirecciones().add(dSaved);
        usuarioRepo.save(u);
        return dSaved;
    }
}
