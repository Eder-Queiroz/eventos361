package web.eventos361.repository.queries.evento;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.eventos361.filter.EventoFilter;
import web.eventos361.model.Evento;
import web.eventos361.repository.pagination.PaginacaoUtil;

public class EventoQueriesImpl implements EventoQueries {

    private static final Logger logger = LoggerFactory.getLogger(EventoQueriesImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Evento> pesquisar(EventoFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Evento> criteriaQuery = builder.createQuery(Evento.class);
        Root<Evento> v = criteriaQuery.from(Evento.class);
        v.alias("eventoAlias");

        List<Predicate> predicateList = new ArrayList<>();
        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(v.get("codigo"), filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(builder.lower(v.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getLocal())) {
            predicateList.add(builder.like(builder.lower(v.get("local")), "%" + filtro.getLocal().toLowerCase() + "%"));
        }
//        predicateList.add(builder.isNull(v.get("finalizouEm")));

        criteriaQuery.select(v).where(predicateList.toArray(new Predicate[0]));
        TypedQuery<Evento> typedQuery = em.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        List<Evento> eventos = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Evento> countRoot = countQuery.from(Evento.class);
        countQuery.select(builder.count(countRoot)).where(predicateList.toArray(new Predicate[0]));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(eventos, pageable, total);
    }
}