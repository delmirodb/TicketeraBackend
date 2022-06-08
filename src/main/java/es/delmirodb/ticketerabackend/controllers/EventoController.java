package es.delmirodb.ticketerabackend.controllers;

import es.delmirodb.ticketerabackend.entities.Asiento;
import es.delmirodb.ticketerabackend.entities.Evento;
import es.delmirodb.ticketerabackend.repositories.AsientoRepository;
import es.delmirodb.ticketerabackend.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private AsientoRepository asientoRepository;

    @GetMapping("/home/eventos")
    public List<Evento> getEventosRandom() {
        return eventoRepository.getRandom();
    }

    @GetMapping("/evento")
    public Evento getEvento(HttpServletRequest request){
        Long id = Long.valueOf(request.getParameter("id"));
        Evento evento = eventoRepository.findByID(id);
        if (evento != null){
            return evento;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/asientosDisponibles")
    public int getAsientosDisponibles(HttpServletRequest request){
        Long tipo = Long.valueOf(request.getParameter("tipo"));
        Long id = Long.valueOf(request.getParameter("id"));
        return asientoRepository.findDisponibles(tipo, id);
    }
}
