package es.delmirodb.ticketerabackend.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import es.delmirodb.ticketerabackend.entities.Cliente;
import es.delmirodb.ticketerabackend.entities.Compra;
import es.delmirodb.ticketerabackend.entities.Evento;
import es.delmirodb.ticketerabackend.entities.Ticket;
import es.delmirodb.ticketerabackend.repositories.ClienteRepository;
import es.delmirodb.ticketerabackend.repositories.CompraRepository;
import es.delmirodb.ticketerabackend.repositories.EventoRepository;
import es.delmirodb.ticketerabackend.repositories.TicketRepository;
import es.delmirodb.ticketerabackend.services.EmailService;
import es.delmirodb.ticketerabackend.services.TokenService;
import es.delmirodb.ticketerabackend.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
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
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EmailService emailService;

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

    @PostMapping("/reenviarEntradas")
    public String reenviarEntradas(HttpServletRequest request) throws Exception {

        long idCliente;
        idCliente = usuarioService.getUserID(request);

        String eventoNombre = request.getParameter("Evento");
        Evento evento = eventoRepository.findByNombre(eventoNombre);
        long idCompra = Integer.parseInt(request.getParameter("idCompra"));
        int nEntradas = Integer.parseInt(request.getParameter("nEntradas"));

        Cliente comprador = clienteRepository.getById(idCliente);
        String eventoDetalles = "";

        if(evento.getTipo().getId() != 4) {
            Object[] ticketsArray = ticketRepository.findTicketsCompra(idCompra).stream().map(Ticket::getId).toArray();
            System.out.println(Arrays.toString(ticketsArray));

            eventoDetalles = "<br/><br/> Aquí tienes tus entradas:";

            for (int i = 0; i < nEntradas; i++) {
                eventoDetalles += "<br/><br/> Entrada " + (i+1) + ": <br/> <img src='https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://compralas.es/validar/"+ ticketsArray[i] +"'/>";
            }
        } else {
            eventoDetalles = "<br/><br/> El evento será retransmitido <a href='https://compralas.es/stream/" + evento.getId() + "'>aquí</a>";
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
        throw new ResponseStatusException(HttpStatus.OK);

    }

}
