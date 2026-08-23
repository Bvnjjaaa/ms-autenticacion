package cl.duoc.jv0101.caso04.autenticacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.caso04.autenticacion.model.Credencial;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {
}
