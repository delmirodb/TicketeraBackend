package es.delmirodb.ticketerabackend.repositories;

import es.delmirodb.ticketerabackend.entities.Admin;
import es.delmirodb.ticketerabackend.entities.Cliente;
import es.delmirodb.ticketerabackend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.email = ?1")
    public Usuario findByEmail(String email);

    @Query("SELECT u FROM Admin u WHERE u.email = ?1")
    public Admin findAdmin(String email);

    @Query("SELECT u FROM Cliente u WHERE u.dni = ?1")
    public Cliente findCliente(String dni);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario SET nombre = ?1, apellido = ?2, email = ?3 WHERE id = ?4")
    public void updateUsuario(String nombre, String apellido, String email, Long id);

}
