package es.delmirodb.ticketerabackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/")
    public String test(){
        return "Hola";
    }

    @GetMapping("/spring")
    public String testPath(){
        return "Hola desde Spring";
    }

}
