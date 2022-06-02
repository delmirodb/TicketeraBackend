package es.delmirodb.ticketerabackend.controllers;

import es.delmirodb.ticketerabackend.entities.Cliente;
import es.delmirodb.ticketerabackend.entities.Usuario;
import es.delmirodb.ticketerabackend.repositories.UsuarioRepository;
import es.delmirodb.ticketerabackend.services.EmailService;
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
public class UserController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public String registrarUsuario(HttpServletRequest request){
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Cliente cliente = new Cliente(nombre, apellido, email, password);
        usuarioService.newCliente(cliente);
        throw new ResponseStatusException(HttpStatus.OK);
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(usuarioService.verificarUsuario(email, password)){
            Usuario usuario = usuarioRepository.findByEmail(email);
            Long UserID = usuario.getId();
            return usuarioService.generateToken(UserID);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/loggedIn")
    public String loggedIn(HttpServletRequest request) throws Exception {
        return usuarioService.verifyToken(request);
    }
}
