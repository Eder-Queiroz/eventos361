package web.eventos361.repository.queries.participante;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import web.eventos361.model.Participante;
import web.eventos361.repository.pagination.PaginacaoUtil;
import web.eventos361.repository.queries.evento.EventoQueriesImpl;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteQueriesImpl implements ParticipanteQueries {

    private static final Logger logger = LoggerFactory.getLogger(EventoQueriesImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Participante> pesquisar(Long idEvento, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Consulta principal
        CriteriaQuery<Participante> criteriaQuery = builder.createQuery(Participante.class);
        Root<Participante> v = criteriaQuery.from(Participante.class);
        v.alias("participanteAlias");
        List<Predicate> predicates = criarParticipantePredicados(idEvento, builder, v);
        criteriaQuery.select(v).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Participante> typedQuery = em.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        List<Participante> participantes = typedQuery.getResultList();

        // Consulta de contagem
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Participante> countRoot = countQuery.from(Participante.class);
        List<Predicate> countParticipantePredicates = criarParticipantePredicados(idEvento, builder, countRoot);
        countQuery.select(builder.count(countRoot)).where(countParticipantePredicates.toArray(new Predicate[0]));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(participantes, pageable, total);
    }

    private List<Predicate> criarParticipantePredicados(Long idEvento, CriteriaBuilder builder, Root<Participante> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("evento").get("id"), idEvento));
        return predicates;
    }

    @Override
    public Page<Participante> pesquisarEventosParticipados(Long idUsuario, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();

        // Consulta principal
        CriteriaQuery<Participante> criteriaQuery = builder.createQuery(Participante.class);
        Root<Participante> v = criteriaQuery.from(Participante.class);
        v.alias("participanteAlias");
        List<Predicate> predicates = criarParticipantePredicadosEventosParticipados(idUsuario, builder, v);
        criteriaQuery.select(v).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Participante> typedQuery = em.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        List<Participante> participantes = typedQuery.getResultList();

        // Consulta de contagem
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Participante> countRoot = countQuery.from(Participante.class);
        List<Predicate> countParticipantePredicates = criarParticipantePredicadosEventosParticipados(idUsuario, builder, countRoot);
        countQuery.select(builder.count(countRoot)).where(countParticipantePredicates.toArray(new Predicate[0]));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(participantes, pageable, total);
    }

    private List<Predicate> criarParticipantePredicadosEventosParticipados(Long idEvento, CriteriaBuilder builder, Root<Participante> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get("usuario").get("id"), idEvento));
        predicates.add(builder.isNotNull(root.get("evento").get("finalizouEm")));
        return predicates;
    }

}
