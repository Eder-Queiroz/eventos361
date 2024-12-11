package web.eventos361.controller;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
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
import web.eventos361.model.Participante;
import web.eventos361.model.Usuario;
import web.eventos361.notificacao.NotificacaoSweetAlert2;
import web.eventos361.notificacao.TipoNotificaoSweetAlert2;
import web.eventos361.pagination.PageWrapper;
import web.eventos361.repository.EventoRepository;
import web.eventos361.service.CadastroUsuarioService;
import web.eventos361.service.EventoService;
import web.eventos361.service.ParticipanteService;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private EventoService eventoService;
    private EventoRepository eventoRepository;
    private CadastroUsuarioService cadastroUsuarioService;
    private ParticipanteService participanteService;

    public EventoController(EventoRepository eventoRepository, EventoService eventoService, CadastroUsuarioService cadastroUsuarioService, ParticipanteService participanteService) {
        this.eventoRepository = eventoRepository;
        this.eventoService = eventoService;
        this.cadastroUsuarioService = cadastroUsuarioService;
        this.participanteService = participanteService;
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
    public String cadastrarNovoUsuario(@Valid Evento evento, BindingResult resultado, Model model,  HtmxResponse.Builder htmxResponse, RedirectAttributes redirectAttributes, Authentication authentication) {
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
        HtmxLocation hl = new HtmxLocation("/eventos/sucesso1");
        hl.setTarget("#main");
        hl.setSwap("outerHTML");
        htmxResponse.location(hl);
        return "mensagem";

    }

    @PostMapping("/abriralterar")
    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    public String abrirAlterarHTMX(Evento evento, Model model, Authentication authentication) {
        logger.info("Abrindo o formulário para alterar o evento: {}", evento);
        String nomeUsuario = authentication.getName();

        if(evento.getUsuario().getNomeUsuario().equals(nomeUsuario)){
            return "evento/alterar :: formulario";
        }

        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Você não pode alterar esse evento!",
                TipoNotificaoSweetAlert2.WARNING, 4000));
        return "evento/pesquisar :: formulario";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Evento evento, BindingResult result, HtmxResponse.Builder htmxResponse) {
        if (result.hasErrors()) {
            logger.info("O evento recebido para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : result.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "evento/alterar :: formulario";
        } else {
            Evento eventoSalvo = eventoService.buscarPorCodigo(evento.getCodigo());
            evento.setUsuario(eventoSalvo.getUsuario());
            eventoService.alterar(evento);
            HtmxLocation hl = new HtmxLocation("/eventos/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso1")
    public String abrirMensagemSucesso1HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Evento criado com sucesso!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "evento/pesquisar :: formulario";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Evento alterada com sucesso!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "evento/pesquisar :: formulario";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @PostMapping("/finalizar")
    public String finalizarHTMX(Long codigo, Model model, Authentication authentication) {

        logger.info("Finalizando o evento com o código: {}", codigo);

        String nomeUsuario = authentication.getName();
        Evento evento = eventoService.buscarPorCodigo(codigo);

        if(!evento.getUsuario().getNomeUsuario().equals(nomeUsuario)){
            model.addAttribute("notificacao", new NotificacaoSweetAlert2("Você não pode finalizar esse evento!",
                    TipoNotificaoSweetAlert2.WARNING, 4000));
            return "evento/pesquisar :: formulario";
        }

        evento.finalizar();
        eventoService.alterar(evento);

        logger.info("Evento finalizado: {}", evento);

        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Evento encerrado com sucesso!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));

        return "evento/pesquisar :: formulario";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @PostMapping("/participar")
    public String participarHTMX(Long codigo, Model model, Authentication authentication) {

        logger.info("Participando do evento com o código: {}", codigo);

        String nomeUsuario = authentication.getName();
        Evento evento = eventoService.buscarPorCodigo(codigo);

        if(evento.getUsuario().getNomeUsuario().equals(nomeUsuario)){
            model.addAttribute("notificacao", new NotificacaoSweetAlert2("Você não pode participar do seu próprio evento!",
                    TipoNotificaoSweetAlert2.WARNING, 4000));
            return "evento/pesquisar :: formulario";
        }

        Usuario usuario = cadastroUsuarioService.pesquisarPorNome(nomeUsuario);

        Participante existeParticipante = participanteService.buscarUsuarioNoEvento(usuario, evento);

        if (existeParticipante != null) {
            model.addAttribute("notificacao", new NotificacaoSweetAlert2("Você já está participando do evento!",
                    TipoNotificaoSweetAlert2.WARNING, 4000));
            return "evento/pesquisar :: formulario";
        }

        Participante participante = new Participante();
        participante.setEvento(evento);
        participante.setUsuario(usuario);
        participanteService.salvar(participante);

        logger.info("Usuário participando do evento: {}", usuario);

        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Você está participando do evento!",
                TipoNotificaoSweetAlert2.SUCCESS, 4000));

        return "evento/pesquisar :: formulario";
    }

}
