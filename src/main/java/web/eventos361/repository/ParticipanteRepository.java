package web.eventos361.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.eventos361.model.Evento;
import web.eventos361.model.Participante;
import web.eventos361.model.Usuario;
import web.eventos361.repository.queries.participante.ParticipanteQueries;

public interface ParticipanteRepository extends JpaRepository<Participante, Long>, ParticipanteQueries {
    Participante findByUsuarioAndEvento(Usuario usuario, Evento evento);
}
