package web.eventos361.controller;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.eventos361.model.Participante;
import web.eventos361.model.Usuario;
import web.eventos361.notificacao.NotificacaoSweetAlert2;
import web.eventos361.notificacao.TipoNotificaoSweetAlert2;
import web.eventos361.pagination.PageWrapper;
import web.eventos361.repository.ParticipanteRepository;
import web.eventos361.service.CadastroUsuarioService;
import web.eventos361.service.ParticipanteService;

@Controller
@RequestMapping("/participantes")
public class ParticipanteController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private ParticipanteService participanteService;
    private ParticipanteRepository participanteRepository;
    private CadastroUsuarioService cadastroUsuarioService;

    public ParticipanteController(ParticipanteService participanteService, ParticipanteRepository participanteRepository, CadastroUsuarioService cadastroUsuarioService) {
        this.participanteService = participanteService;
        this.participanteRepository = participanteRepository;
        this.cadastroUsuarioService = cadastroUsuarioService;
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @PostMapping("/ver")
    public String verParticipantes(Long idEvento, Model model, @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
                                   HttpServletRequest request) {
        try {
            logger.info("buscando participantes do evento {}", idEvento);
            Page<Participante> participantes = participanteRepository.pesquisar(idEvento, pageable);
            PageWrapper<Participante> paginaWrapper = new PageWrapper<>(participantes, request);
            logger.info("participantes encontrados: {}", paginaWrapper.getConteudo());
            model.addAttribute("pagina", paginaWrapper);
            return "participante/ver :: tabela";
        }catch (Exception e) {
            logger.error("Erro ao buscar participantes: {}", e);
            model.addAttribute("notificacao", new NotificacaoSweetAlert2("Erro ao buscar participantes",
                    TipoNotificaoSweetAlert2.ERROR, 4000));
            return "evento/pesquisar :: formulario";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/participado")
    public String participado(Model model, Authentication authentication, @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
                              HttpServletRequest request) {
        try {
            logger.info("iniciando busca de eventos participados");
            String nomeUsuario = authentication.getName();
            Usuario usuario = cadastroUsuarioService.pesquisarPorNome(nomeUsuario);
            Page<Participante> participantes = participanteRepository.pesquisarEventosParticipados(usuario.getCodigo(), pageable);
            PageWrapper<Participante> paginaWrapper = new PageWrapper<>(participantes, request);
            logger.info("eventos participados encontrados: {}", paginaWrapper.getConteudo());
            model.addAttribute("pagina", paginaWrapper);
            return "participante/participado :: tabela";
        }catch (Exception e) {
            logger.error("Erro ao participar do evento: {}", e);
            model.addAttribute("notificacao", new NotificacaoSweetAlert2("Erro ao buscar eventos participados",
                    TipoNotificaoSweetAlert2.ERROR, 4000));
            return "evento/pesquisar :: formulario";
        }
    }
}
