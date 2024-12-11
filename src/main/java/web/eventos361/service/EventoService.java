package web.eventos361.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.eventos361.model.Evento;
import web.eventos361.repository.EventoRepository;

@Service
@Transactional
public class EventoService {

    private EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public void salvar(Evento evento) {
        eventoRepository.save(evento);
    }

    public void alterar(Evento evento) {
        eventoRepository.save(evento);
    }

    public Evento buscarPorCodigo(Long codigo) {
        return eventoRepository.findById(codigo).orElse(null);
    }
}
