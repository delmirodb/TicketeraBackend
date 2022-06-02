package es.delmirodb.ticketerabackend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Admin extends Usuario {

    private int nivel;

    public Admin(String nombre, String apellido, String email, String password, int nivel){
        super(nombre, apellido, email, password);
        this.nivel = nivel;
    }

}
