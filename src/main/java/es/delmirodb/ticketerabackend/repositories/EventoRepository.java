package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e WHERE e.nombre = ?1")
    public Evento findByNombre(String nombre);

    @Query(value = "SELECT * FROM Evento ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
    public List<Evento> getRandom();

    @Query("SELECT e FROM Evento e WHERE e.id = ?1")
    public Evento findByID(Long id);

}
