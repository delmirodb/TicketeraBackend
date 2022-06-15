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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.stream.Stream;


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
    @Autowired
    private EmailService emailService;
    @Autowired
    ResourceLoader resourceLoader;


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

    public Long comprarEntradas(HttpServletRequest request) throws Exception {

        long idCliente;
        idCliente = getUserID(request);

        String eventoNombre = request.getParameter("Evento");
        Evento evento = eventoRepository.findByNombre(eventoNombre);
        long tipo = Integer.parseInt(request.getParameter("Tipo"));
        int nEntradas = Integer.parseInt(request.getParameter("nEntradas"));

        double precio = 0;
        if (tipo == 1) {
            precio = (nEntradas) * 10.00;
        } else if (tipo == 2) {
            precio = (nEntradas) * 30.00;
        }

        Cliente comprador = clienteRepository.getById(idCliente);
        Compra compra = new Compra(precio, comprador);
        compraRepository.save(compra);
        String eventoDetalles = "";

        for (int i = 1; i <= nEntradas; i++) {
            Cliente cliente = new Cliente(request.getParameter("nombre" + i), request.getParameter("apellido" + i), request.getParameter("dni" + i));
            Cliente clienteExistente = usuarioRepository.findCliente(cliente.getDni());
            Asiento asiento = asientoRepository.findAsiento(tipo, evento.getId());
            asiento.setEstado(estadoAsientoRepository.getById(2L));
            asientoRepository.save(asiento);

            EstadoTicket estadoTicket = estadoTicketRepository.getById(1L);

            if(evento.getTipo().getId() == 4){
                Ticket ticket = new Ticket(asiento, evento, compra, comprador, estadoTicket);
                ticketRepository.save(ticket);
                eventoDetalles = "<br/><br/> El evento será retransmitido <a href='http://localhost:3000/stream/" + evento.getId() + "'>aquí</a>";
            } else {
                if (clienteExistente == null) {
                    usuarioRepository.save(cliente);
                    Ticket ticket = new Ticket(asiento, evento, compra, cliente, estadoTicket);
                    ticketRepository.save(ticket);
                } else {
                    Ticket ticket = new Ticket(asiento, evento, compra, clienteExistente, estadoTicket);
                    ticketRepository.save(ticket);
                }
            }
        }

        String emailBody = "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <title></title>\n" +
                "  <!--[if mso]>\n" +
                "  <style>\n" +
                "    table {border-collapse:collapse;border-spacing:0;border:none;margin:0;}\n" +
                "    div, td {padding:0;}\n" +
                "    div {margin:0 !important;}\n" +
                "\t</style>\n" +
                "  <noscript>\n" +
                "    <xml>\n" +
                "      <o:OfficeDocumentSettings>\n" +
                "        <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "      </o:OfficeDocumentSettings>\n" +
                "    </xml>\n" +
                "  </noscript>\n" +
                "  <![endif]-->\n" +
                "  <style>\n" +
                "    table,\n" +
                "    td,\n" +
                "    div,\n" +
                "    h1,\n" +
                "    p {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "    }\n" +
                "\n" +
                "    @media screen and (max-width: 530px) {\n" +
                "      .unsub {\n" +
                "        display: block;\n" +
                "        padding: 8px;\n" +
                "        margin-top: 14px;\n" +
                "        border-radius: 6px;\n" +
                "        background-color: #555555;\n" +
                "        text-decoration: none !important;\n" +
                "        font-weight: bold;\n" +
                "      }\n" +
                "\n" +
                "      .col-lge {\n" +
                "        max-width: 100% !important;\n" +
                "      }\n" +
                "    }\n" +
                "\n" +
                "    @media screen and (min-width: 531px) {\n" +
                "      .col-sml {\n" +
                "        max-width: 27% !important;\n" +
                "      }\n" +
                "\n" +
                "      .col-lge {\n" +
                "        max-width: 73% !important;\n" +
                "      }\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"margin:0;padding:0;word-spacing:normal;background-color:#939297;\">\n" +
                "  <div role=\"article\" aria-roledescription=\"email\" lang=\"en\"\n" +
                "    style=\"text-size-adjust:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;background-color:#939297;\">\n" +
                "    <table role=\"presentation\" style=\"width:100%;border:none;border-spacing:0;\">\n" +
                "      <tr>\n" +
                "        <td align=\"center\" style=\"padding:0;\">\n" +
                "          <!--[if mso]>\n" +
                "          <table role=\"presentation\" align=\"center\" style=\"width:600px;\">\n" +
                "          <tr>\n" +
                "          <td>\n" +
                "          <![endif]-->\n" +
                "          <table role=\"presentation\"\n" +
                "            style=\"width:94%;max-width:600px;border:none;border-spacing:0;text-align:left;font-family:Arial,sans-serif;font-size:16px;line-height:22px;color:#363636;\">\n" +
                "            <tr>\n" +
                "              <td style=\"padding:40px 30px 30px 30px;text-align:center;font-size:24px;font-weight:bold;\">\n" +
                "                <a href=\"http://www.example.com/\" style=\"text-decoration:none;\"><img\n" +
                "                    src=\"https://compralas.es/img/compralas%20logo.png\" width=\"165\" alt=\"Logo\"\n" +
                "                    style=\"width:165px;max-width:80%;height:auto;border:none;text-decoration:none;color:#ffffff;\"></a>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td style=\"padding:30px;background-color:#ffffff;\">\n" +
                "                <h1\n" +
                "                  style=\"margin-top:0;margin-bottom:16px;font-size:26px;line-height:32px;font-weight:bold;letter-spacing:-0.02em;\">\n" +
                "                  Gracias por tu compra!</h1>\n" +
                "                <p>Hola,</p>\n" +
                "                <p style=\"margin:0;\">Has realizado tu compra sastisfactoriamente para el siguiente evento:</p>\n" +
                "                <h2\n" +
                "                  style=\"margin-bottom:5px;line-height:32px;font-weight:bold;letter-spacing:-0.02em;\">\n" +
                "                  " + eventoNombre + " - " + evento.getCiudad() + " </h2>\n" +
                "                <h3\n" +
                "                  style=\"margin-top: 0px;margin-bottom:16px;line-height:32px;font-weight:bold;letter-spacing:-0.02em;\">\n" +
                "                  " + evento.getFecha() + "</h3>\n" +
                "                <p> " + nEntradas + " entrada(s) " + eventoDetalles +"</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "\n" +
                "            <tr>\n" +
                "              <td style=\"padding:30px;text-align:center;font-size:12px;background-color:#404040;color:#cccccc;\">\n" +
                "                <p style=\"margin:0;font-size:14px;line-height:20px;\">&copy; Cómpralas 2022 <br<p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "          <!--[if mso]>\n" +
                "          </td>\n" +
                "          </tr>\n" +
                "          </table>\n" +
                "          <![endif]-->\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        emailService.sendSimpleMessage(comprador.getEmail(), "Tu compra de tickets.", emailBody);

        return compra.getId();
    }

    public String generateToken(Long id) throws Exception {

        // File file = ResourceUtils.getFile("classpath:private_key.der");
        // String clavePrivada = file.getPath();

        Resource resource = resourceLoader.getResource("classpath:private_key.der");
        InputStream inputStream = resource.getInputStream();

        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

        RSAPrivateKey privateKey = (RSAPrivateKey) TokenService.getPrivate(bytes);

        try {
            Date dt = new Date();
            Date expires = new Date(dt.getTime() + (60 * 60 * 1000));
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

        // File file = ResourceUtils.getFile("classpath:public_key.der");
        // String clavePublica = file.getPath();

        Resource resource = resourceLoader.getResource("classpath:public_key.der");
        InputStream inputStream = resource.getInputStream();

        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

        RSAPublicKey publicKey = (RSAPublicKey) TokenService.getPublic(bytes);
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

    public Long getUserID(HttpServletRequest request) throws Exception {
        String token = request.getParameter("usuarioSessionID");
        if(token == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // File file = ResourceUtils.getFile("classpath:public_key.der");
        // String clavePublica = file.getPath();

        Resource resource = resourceLoader.getResource("classpath:public_key.der");
        InputStream inputStream = resource.getInputStream();

        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

        RSAPublicKey publicKey = (RSAPublicKey) TokenService.getPublic(bytes);
        long idCliente;
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("compralasVerify")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String payload = String.valueOf(jwt.getClaim("id"));
            idCliente = Long.parseLong(payload);
            return idCliente;
        } catch (JWTVerificationException exception) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

}
