package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Compra;
import es.delmirodb.ticketerabackend.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT DISTINCT c.id AS id, t.evento.nombre AS evento, c.importe AS importe FROM Compra c JOIN Ticket t ON t.compra.id = c.id WHERE c.cliente.id = ?1")
    public List<Object> findCompraCliente(Long id);

    @Query("SELECT c.cliente.id FROM Compra c WHERE c.id = ?1")
    public Long findClienteID(Long id);

    @Query("SELECT c FROM Compra c WHERE c.id = ?1")
    public Compra findByID(Long id);

}
