package es.delmirodb.ticketerabackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@RestController
public class Controller {

    @GetMapping("/")
    public String test(){
        return "Hola";
    }

    @GetMapping("/private")
    public String testPath() throws Exception {

        RSAPrivateKey privateKey = (RSAPrivateKey) Service.getPrivate("src/main/resources/keys/private_key.der");

        try {
            Date dt = new Date();
            Date expires = new Date(dt.getTime() + (120000));
            Algorithm algorithm = Algorithm.RSA256(null, privateKey);
            String token = JWT.create()
                    .withIssuer("compralasVerify")
                    .withClaim("nombre", "Delmiro")
                    .withExpiresAt(expires)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return "Error";
    }

    @PostMapping("/verify")
    public String verifyPath(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getParameter("token");
        RSAPublicKey publicKey = (RSAPublicKey) Service.getPublic("src/main/resources/keys/public_key.der");
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("compralasVerify")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return Service.decodeToJson(jwt.getPayload());
        } catch (JWTVerificationException exception){
            response.setStatus(403);
            return "Error 403";
        }
    }

}
