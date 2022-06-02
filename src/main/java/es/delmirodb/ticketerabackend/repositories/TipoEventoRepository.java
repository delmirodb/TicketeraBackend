package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoEventoRepository extends JpaRepository<TipoEvento, Long> {
}
