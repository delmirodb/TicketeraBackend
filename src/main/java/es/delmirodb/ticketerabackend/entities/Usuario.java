package es.delmirodb.ticketerabackend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String email;

    private String password;

    public Usuario(String name, String apellido) {
        this.nombre = name;
        this.apellido = apellido;
    }

    public Usuario(String name, String apellido, String email, String password) {
        this.nombre = name;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }

}
