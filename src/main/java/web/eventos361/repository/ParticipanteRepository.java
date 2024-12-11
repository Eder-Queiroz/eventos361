package web.eventos361.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.eventos361.model.Participante;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
}
