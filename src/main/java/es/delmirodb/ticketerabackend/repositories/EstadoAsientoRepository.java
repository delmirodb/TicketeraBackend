package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.EstadoAsiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoAsientoRepository extends JpaRepository<EstadoAsiento, Long> {
}
