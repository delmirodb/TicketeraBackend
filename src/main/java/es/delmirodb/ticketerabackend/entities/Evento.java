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
    private String ciudad;
    private String descripcion;

    @ManyToOne
    private TipoEvento tipo;

    public Evento(String nombre, String fecha, String ciudad, String descripcion, TipoEvento tipo){
        this.nombre = nombre;
        this.fecha = fecha;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

}
