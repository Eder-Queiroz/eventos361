package web.eventos361.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.eventos361.model.Evento;
import web.eventos361.repository.queries.evento.EventoQueries;

public interface EventoRepository extends JpaRepository<Evento, Long>, EventoQueries {
}
