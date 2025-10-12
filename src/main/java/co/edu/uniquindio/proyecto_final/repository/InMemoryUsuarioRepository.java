package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Usuario;

import java.util.*;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<String, Usuario> storage = new HashMap<>();

    @Override
    public Usuario save(Usuario u) {
        storage.put(u.getIdUsuario(), u);
        return u;
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
