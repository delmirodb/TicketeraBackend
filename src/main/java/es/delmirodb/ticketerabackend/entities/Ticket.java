package es.delmirodb.ticketerabackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Asiento asiento;

    @ManyToOne
    private Evento evento;

    @ManyToOne
    private Compra compra;

    @OneToOne
    private Cliente propietario;

    @ManyToOne
    private EstadoTicket estado;

    public Ticket(Asiento asiento, Evento evento, Compra compra, Cliente propietario, EstadoTicket estado){
        this.asiento = asiento;
        this.evento = evento;
        this.compra = compra;
        this.propietario = propietario;
        this.estado = estado;
    }

}
