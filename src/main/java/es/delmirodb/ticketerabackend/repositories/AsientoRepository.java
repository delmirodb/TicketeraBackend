package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AsientoRepository extends JpaRepository<Asiento, Long> {

    @Query("SELECT COUNT(a) FROM Asiento a WHERE a.estado = '1' AND a.tipo.id = ?1 AND a.evento.id = ?2")
    public int findDisponibles(Long tipo, Long id);

    @Query(value = "SELECT * FROM asiento a WHERE a.estado_id = '1' AND a.tipo_id = ?1 AND a.evento_id = ?2 LIMIT 1", nativeQuery = true)
    public Asiento findAsiento(Long tipo, Long id);

}
