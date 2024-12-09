package web.eventos361.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import web.eventos361.filter.EventoFilter;
import web.eventos361.model.Evento;
import web.eventos361.pagination.PageWrapper;
import web.eventos361.repository.EventoRepository;
import web.eventos361.service.EventoService;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private EventoService eventoService;
    private EventoRepository eventoRepository;

    public EventoController(EventoRepository eventoRepository, EventoService eventoService) {
        this.eventoRepository = eventoRepository;
        this.eventoService = eventoService;
    }

    @GetMapping("/buscar")
    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    public String buscarEventos(Model model) {
        return "evento/buscar :: formulario";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "evento/pesquisar :: formulario";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(EventoFilter filtro, Model model,
                                @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
                                HttpServletRequest request) {
        logger.info("Filtrando eventos: {}", filtro);
        Page<Evento> pagina = eventoRepository.pesquisar(filtro, pageable);
        PageWrapper<Evento> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "evento/eventos :: tabela";
    }


}
