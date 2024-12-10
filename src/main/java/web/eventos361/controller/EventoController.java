package web.eventos361.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.eventos361.filter.EventoFilter;
import web.eventos361.model.Evento;
import web.eventos361.model.Usuario;
import web.eventos361.pagination.PageWrapper;
import web.eventos361.repository.EventoRepository;
import web.eventos361.service.CadastroUsuarioService;
import web.eventos361.service.EventoService;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private EventoService eventoService;
    private EventoRepository eventoRepository;
    private CadastroUsuarioService cadastroUsuarioService;

    public EventoController(EventoRepository eventoRepository, EventoService eventoService, CadastroUsuarioService cadastroUsuarioService) {
        this.eventoRepository = eventoRepository;
        this.eventoService = eventoService;
        this.cadastroUsuarioService = cadastroUsuarioService;
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

    @GetMapping("/novo")
    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    public String abrirCadastroVacinaHTMX(Evento evento, Model model) {
        return "evento/novo :: formulario";
    }

    @PostMapping("/novo")
    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    public String cadastrarNovoUsuario(@Valid Evento evento, BindingResult resultado, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        logger.info("Recebendo um novo evento para cadastrar: {}", evento);
        if (resultado.hasErrors()) {
            logger.info("O Evento recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "evento/novo :: formulario";
        }
            logger.info("O evento recebido para cadastrar é válido.");
        String nomeUsuario = authentication.getName();
        Usuario usuario = cadastroUsuarioService.pesquisarPorNome(nomeUsuario);

        logger.info("Usuário que cadastrou: {}", usuario.getNomeUsuario());

        evento.setUsuario(usuario);
        eventoService.salvar(evento);
        redirectAttributes.addAttribute("mensagem", "Cadastro de usuário efetuado com sucesso.");
        return "evento/pesquisar :: formulario";

    }

}
