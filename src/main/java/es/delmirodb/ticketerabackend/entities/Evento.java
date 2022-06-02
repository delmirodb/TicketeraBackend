package es.delmirodb.ticketerabackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String fecha;

    @ManyToOne
    private TipoEvento tipo;

    public Evento(String nombre, String fecha, TipoEvento tipo){
        this.nombre = nombre;
        this. fecha = fecha;
        this.tipo = tipo;
    }

}
