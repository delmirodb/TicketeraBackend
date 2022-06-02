package es.delmirodb.ticketerabackend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn
public class Cliente extends Usuario{

    @Column(unique = true)
    private String dni;

    @OneToMany
    private List<Ticket> tickets;

    @OneToMany
    private List<Compra> compras;

    public Cliente(String nombre, String apellido, String dni){
        super(nombre, apellido);
        this.dni = dni;
    }

    public Cliente(String nombre, String apellido, String email, String password){
        super(nombre, apellido, email, password);
    }

}
