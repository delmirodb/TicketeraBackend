package es.delmirodb.ticketerabackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import es.delmirodb.ticketerabackend.entities.*;
import es.delmirodb.ticketerabackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AsientoRepository asientoRepository;
    @Autowired
    private EstadoAsientoRepository estadoAsientoRepository;
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private EstadoTicketRepository estadoTicketRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CompraRepository compraRepository;

    public void newCliente(Cliente cliente){
        String encryptedPass = passwordEncoder.encode(cliente.getPassword());
        cliente.setPassword(encryptedPass);
        usuarioRepository.save(cliente);
    }

    public Admin newAdmin(Admin admin){
        String encryptedPass = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encryptedPass);
        return admin;
    }

    public boolean verificarUsuario(String email, String password) {
        Usuario user = usuarioRepository.findByEmail(email);
        if(user != null){
            return passwordEncoder.matches(password, user.getPassword());
        } else return false;
    }

    public boolean isAdmin(Usuario usuario){
        Admin admin = usuarioRepository.findAdmin(usuario.getEmail());
        return admin != null;
    }

    public void comprarEntradas(HttpServletRequest request){
        HttpSession session = request.getSession();
        Long idCliente = (Long) session.getAttribute("usuarioSessionID");

        String eventoNombre = request.getParameter("Evento");
        Evento evento = eventoRepository.findByNombre(eventoNombre);
        long tipo = Integer.parseInt(request.getParameter("Tipo"));
        int nEntradas = Integer.parseInt(request.getParameter("nEntradas"));

        double precio = 0;
        if(tipo == 1){
            precio = (nEntradas+1) * 10.00;
        } else if(tipo == 2){
            precio = (nEntradas+1) * 30.00;
        }

        Cliente comprador = clienteRepository.getById(idCliente);
        Compra compra = new Compra(precio, comprador);
        compraRepository.save(compra);

        for (int i = 0; i < nEntradas+1; i++) {
            Cliente cliente = new Cliente(request.getParameter("nombre" + i), request.getParameter("apellido" + i), request.getParameter("dni" + i));
            Cliente clienteExistente = usuarioRepository.findCliente(cliente.getDni());
            Asiento asiento = asientoRepository.findAsiento(tipo, 2L);
            asiento.setEstado(estadoAsientoRepository.getById(2L));
            asientoRepository.save(asiento);

            EstadoTicket estadoTicket = estadoTicketRepository.getById(1L);
            if(clienteExistente == null){
                usuarioRepository.save(cliente);
                Ticket ticket = new Ticket(asiento, evento, compra, cliente, estadoTicket);
                ticketRepository.save(ticket);
            } else {
                Ticket ticket = new Ticket(asiento, evento, compra, clienteExistente, estadoTicket);
                ticketRepository.save(ticket);
            }

        }
    }

    public String generateToken(Long id) throws Exception {

        RSAPrivateKey privateKey = (RSAPrivateKey) TokenService.getPrivate("src/main/resources/keys/private_key.der");

        try {
            Date dt = new Date();
            Date expires = new Date(dt.getTime() + (120000));
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            return JWT.create()
                    .withIssuer("compralasVerify")
                    .withClaim("id", id)
                    .withExpiresAt(expires)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public String verifyToken(HttpServletRequest request) throws Exception {
        String token = request.getParameter("token");
        RSAPublicKey publicKey = (RSAPublicKey) TokenService.getPublic("src/main/resources/keys/public_key.der");
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("compralasVerify")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return TokenService.decodeToJson(jwt.getPayload());
        } catch (JWTVerificationException exception){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
