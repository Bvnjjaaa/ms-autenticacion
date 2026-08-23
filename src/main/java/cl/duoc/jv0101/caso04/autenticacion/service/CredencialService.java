package cl.duoc.jv0101.caso04.autenticacion.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import cl.duoc.jv0101.caso04.autenticacion.model.Credencial;
import cl.duoc.jv0101.caso04.autenticacion.repository.CredencialRepository;

@Service
public class CredencialService {

    private final CredencialRepository repository;

    public CredencialService(CredencialRepository repository) {
        this.repository = repository;
    }

    public List<Credencial> findAll() {
        return repository.findAll();
    }

    public Optional<Credencial> findById(Long id) {
        return repository.findById(id);
    }

    public Credencial create(Credencial recurso) {
        return repository.save(recurso);
    }

    public Optional<Credencial> update(Long id, Credencial datos) {
        return repository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setUsuario(datos.getUsuario());
            existente.setRol(datos.getRol());
            return repository.save(existente);
        });
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(existente -> {
            repository.delete(existente);
            return true;
        }).orElse(false);
    }
}
