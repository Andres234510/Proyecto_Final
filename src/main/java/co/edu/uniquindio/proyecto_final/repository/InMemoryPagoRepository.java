package co.edu.uniquindio.proyecto_final.repository;

import co.edu.uniquindio.proyecto_final.model.Pago;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryPagoRepository implements PagoRepository {
    private final Map<String, Pago> storage = new HashMap<>();

    @Override
    public Pago save(Pago p) {
        storage.put(p.getIdPago(), p);
        return p;
    }

    @Override
    public Optional<Pago> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Pago> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Pago> findByEnvioId(String idEnvio) {
        return storage.values().stream()
                .filter(p -> p.getIdEnvio() != null && p.getIdEnvio().equals(idEnvio))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }
}
