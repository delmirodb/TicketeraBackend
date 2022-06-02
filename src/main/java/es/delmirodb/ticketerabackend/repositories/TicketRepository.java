package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.compra.id = ?1")
    public List<Ticket> findTicketsCompra(Long id);

}
