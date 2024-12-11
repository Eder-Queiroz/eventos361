package web.eventos361.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.eventos361.model.Evento;
import web.eventos361.model.Participante;
import web.eventos361.model.Usuario;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    Participante findByUsuarioAndEvento(Usuario usuario, Evento evento);
}
