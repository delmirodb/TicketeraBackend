package es.delmirodb.ticketerabackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asiento {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private TipoAsiento tipo;

    @OneToOne
    private EstadoAsiento estado;

    @ManyToOne
    private Evento evento;

    public Asiento(TipoAsiento tipo, EstadoAsiento estado, Evento evento){
        this.tipo = tipo;
        this.estado = estado;
        this.evento = evento;
    }

}
