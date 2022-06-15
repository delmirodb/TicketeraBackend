package es.delmirodb.ticketerabackend.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import es.delmirodb.ticketerabackend.entities.Compra;
import es.delmirodb.ticketerabackend.entities.Evento;
import es.delmirodb.ticketerabackend.entities.Ticket;
import es.delmirodb.ticketerabackend.repositories.CompraRepository;
import es.delmirodb.ticketerabackend.repositories.TicketRepository;
import es.delmirodb.ticketerabackend.services.TokenService;
import es.delmirodb.ticketerabackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UsuarioService usuarioService;

    // @GetMapping("/compra")
    // public Compra getCompra(HttpServletRequest request){
    //     Long id = Long.valueOf(request.getParameter("id"));
    //     Compra compra = compraRepository.findByID(id);
    //     if (compra != null){
    //         return compra;
    //     } else {
    //         throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    //     }
    // }

    @PostMapping("/compra")
    public Map<String,Object> getTestMap(HttpServletRequest request) throws Exception {

        long idCliente;
        idCliente = usuarioService.getUserID(request);

        Long id = Long.valueOf(request.getParameter("id"));
        Compra compra = compraRepository.findByID(id);
        long idCliemteCompra = compra.getCliente().getId();

        if( idCliente == idCliemteCompra){
            List<Ticket> tickets = ticketRepository.findTicketsCompra(id);
            Map<String,Object> map= new HashMap<>();
            map.put("Compra", compra);
            map.put("Tickets", tickets);
            return map;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/usuario/compras")
    public List<Object> getComprasUsuario(HttpServletRequest request) throws Exception {

        long idCliente;
        idCliente = usuarioService.getUserID(request);

        return compraRepository.findCompraCliente(idCliente);
    }

}
