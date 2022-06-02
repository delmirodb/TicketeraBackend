package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.TipoAsiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoAsientoRepository extends JpaRepository<TipoAsiento, Long> {
}
