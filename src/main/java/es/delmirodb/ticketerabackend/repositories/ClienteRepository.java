package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
