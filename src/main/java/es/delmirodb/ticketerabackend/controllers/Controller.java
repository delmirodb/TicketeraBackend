package es.delmirodb.ticketerabackend.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import es.delmirodb.ticketerabackend.entities.Cliente;
import es.delmirodb.ticketerabackend.entities.Usuario;
import es.delmirodb.ticketerabackend.repositories.EventoRepository;
import es.delmirodb.ticketerabackend.repositories.UsuarioRepository;
import es.delmirodb.ticketerabackend.services.EmailService;
import es.delmirodb.ticketerabackend.services.TokenService;
import es.delmirodb.ticketerabackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class Controller {

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String test(){
        return "Hola";
    }
    @GetMapping("/email")
    public String email(){

        emailService.sendSimpleMessage("delmirodb@gmail.com", "Prueba", "Hola");
        return "Enviado";
    }

}
