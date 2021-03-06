package es.delmirodb.ticketerabackend.services;

import es.delmirodb.ticketerabackend.entities.*;
import es.delmirodb.ticketerabackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader implements CommandLineRunner {

    private final EstadoAsientoRepository EstadoAsiento;
    private final EstadoTicketRepository EstadoTicket;
    private final TipoEventoRepository TipoEvento;
    private final UsuarioRepository UsuarioRepository;
    private final TipoAsientoRepository TipoAsiento;
    private final EventoRepository Evento;
    private final AsientoRepository Asiento;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    public DatabaseLoader(
            EstadoAsientoRepository EstadoAsiento,
            EstadoTicketRepository EstadoTicket,
            TipoEventoRepository TipoEvento,
            UsuarioRepository UsuarioRepository,
            TipoAsientoRepository TipoAsiento,
            EventoRepository Evento,
            AsientoRepository Asiento

    ){
        this.EstadoAsiento = EstadoAsiento;
        this.EstadoTicket = EstadoTicket;
        this.TipoEvento = TipoEvento;
        this.UsuarioRepository = UsuarioRepository;
        this.TipoAsiento = TipoAsiento;
        this.Evento = Evento;
        this.Asiento = Asiento;
    }

    @Override
    public void run(String... strings) {

        if (UsuarioRepository.findById(1L).orElse(null) == null){
            this.EstadoAsiento.save(new EstadoAsiento(1L, "Disponible"));
            this.EstadoAsiento.save(new EstadoAsiento(2L, "Comprado"));
            this.EstadoAsiento.save(new EstadoAsiento(3L, "Reservado"));

            this.EstadoTicket.save(new EstadoTicket(1L, "Válido"));
            this.EstadoTicket.save(new EstadoTicket(2L, "Utilizado"));
            this.EstadoTicket.save(new EstadoTicket(3L, "Reembolsado"));

            this.TipoEvento.save(new TipoEvento(1L, "Obra"));
            this.TipoEvento.save(new TipoEvento(2L, "Stand-up"));
            this.TipoEvento.save(new TipoEvento(3L, "Concierto"));
            this.TipoEvento.save(new TipoEvento(4L, "Streaming"));
            this.TipoEvento.save(new TipoEvento(5L, "Deportivo"));

            Admin admin = new Admin("admin", "admin", "admin@compralas.es", "admin", 3);
            this.UsuarioRepository.save(usuarioService.newAdmin(admin));
            this.Evento.save(new Evento("Delmiro's Show", "17/06/2022", "Madrid", "Show de Delmiro", TipoEvento.getById(2L)));
            this.Evento.save(new Evento("Delmiro's Streaming", "17/06/2022", "Streaming", "Streaming de Delmiro", TipoEvento.getById(4L)));
            this.Evento.save(new Evento("Reggaeton Beach Festival", "2/07/2022", "Madrid", "Reggaeton Beach Festival - Recinto Mad Cool", TipoEvento.getById(3L)));
            this.Evento.save(new Evento("Mundial de globos", "1/07/2022", "Streaming", "Segunda edición del mundial de globos", TipoEvento.getById(4L)));
            this.Evento.save(new Evento("Champions League Final", "10/06/2022", "Estambul", "Final de la Champions League", TipoEvento.getById(5L)));
            this.Evento.save(new Evento("Wimbledon Final", "10/07/2022", "Londres", "Final de Wimbledon", TipoEvento.getById(5L)));

            this.TipoAsiento.save(new TipoAsiento(1L, "General", 10.00));
            this.TipoAsiento.save(new TipoAsiento(2L, "VIP", 30.00));

            for(int x = 2; x < 8; x++){
                for (int i = 0; i < 20; i++){
                    this.Asiento.save(new Asiento(TipoAsiento.getById(1L), EstadoAsiento.getById(1L), Evento.getById((long) x)));
                }
            }
            this.Asiento.save(new Asiento(TipoAsiento.getById(2L), EstadoAsiento.getById(1L), Evento.getById(2L)));

        }

    }
}
