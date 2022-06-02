package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT c FROM Compra c WHERE c.cliente.id = ?1")
    public List<Compra> findCompraCliente(Long id);

    @Query("SELECT c.cliente.id FROM Compra c WHERE c.id = ?1")
    public Long findClienteID(Long id);

}
