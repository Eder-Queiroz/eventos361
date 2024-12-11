package web.eventos361.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.eventos361.model.Participante;
import web.eventos361.repository.ParticipanteRepository;

@Service
@Transactional
public class ParticipanteService {

    private ParticipanteRepository participanteRepository;

    public ParticipanteService(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    public void salvar(Participante participante) {
        participanteRepository.save(participante);
    }

    public void alterar(Participante participante) {
        participanteRepository.save(participante);
    }

}
