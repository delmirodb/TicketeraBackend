package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e WHERE e.nombre = ?1")
    public Evento findByNombre(String nombre);

}
