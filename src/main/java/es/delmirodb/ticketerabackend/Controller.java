package es.delmirodb.ticketerabackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPrivateKey;

@RestController
public class Controller {

    @GetMapping("/")
    public String test(){
        return "Hola";
    }

    @GetMapping("/spring")
    public String testPath() throws Exception {

        RSAPrivateKey privateKey = (RSAPrivateKey) Service.getPrivate("keys/jwtRS256.key");

        try {
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return "Error";
    }



}
