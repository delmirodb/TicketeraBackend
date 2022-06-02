package es.delmirodb.ticketerabackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compra {

    @Id
    @GeneratedValue
    private Long id;

    private double importe;
    private boolean reembolsado;

    @ManyToOne
    private Cliente cliente;

    @OneToMany
    private List<Ticket> tickets;

    public Compra(double importe, Cliente cliente){
        this.importe = importe;
        this.reembolsado = false;
        this.cliente = cliente;
    }

}
