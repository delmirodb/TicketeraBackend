package es.delmirodb.ticketerabackend.controllers;

import es.delmirodb.ticketerabackend.entities.EstadoTicket;
import es.delmirodb.ticketerabackend.entities.Ticket;
import es.delmirodb.ticketerabackend.repositories.EstadoTicketRepository;
import es.delmirodb.ticketerabackend.repositories.TicketRepository;
import es.delmirodb.ticketerabackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class TicketController {

    @Autowired
    UsuarioService usuarioService;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    EstadoTicketRepository estadoTicketRepository;

    @PostMapping("/validar")
    public String validarTicket(HttpServletRequest request) throws Exception {

        Long idCliente = usuarioService.getUserID(request);
        Long ticketID = Long.valueOf(request.getParameter("id"));

        if(idCliente == 1L){
            Ticket ticket = ticketRepository.getById(ticketID);
            if(ticket.getEstado().getId() != 1L) {
                return "Utilizado";
            } else {
                EstadoTicket estado = estadoTicketRepository.getById(2L);
                ticket.setEstado(estado);
                ticketRepository.save(ticket);
                throw new ResponseStatusException(HttpStatus.OK);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
