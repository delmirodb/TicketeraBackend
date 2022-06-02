package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoTicketRepository extends JpaRepository<EstadoTicket, Long> {
}
