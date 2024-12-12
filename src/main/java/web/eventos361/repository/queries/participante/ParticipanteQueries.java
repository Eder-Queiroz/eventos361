package web.eventos361.repository.queries.participante;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.eventos361.model.Participante;

public interface ParticipanteQueries {

    Page<Participante> pesquisar(Long idEvento, Pageable pageable);
    Page<Participante> pesquisarEventosParticipados(Long idUsuario, Pageable pageable);

}
