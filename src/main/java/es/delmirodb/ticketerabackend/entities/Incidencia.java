package es.delmirodb.ticketerabackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Incidencia {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Compra compra;

    @OneToOne
    private Cliente cliente;

}
