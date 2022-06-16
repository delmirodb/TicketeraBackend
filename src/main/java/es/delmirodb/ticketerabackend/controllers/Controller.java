package es.delmirodb.ticketerabackend.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class Controller {

    @GetMapping("/")
    public String test(){
        return "Hola, esta es la API de Compralas.es";
    }

}
