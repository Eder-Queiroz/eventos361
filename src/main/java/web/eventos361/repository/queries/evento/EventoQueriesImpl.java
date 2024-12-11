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
        TypedQuery<Evento> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();
        List<Predicate> predicateListTotal = new ArrayList<>();
        Predicate[] predArray;
        Predicate[] predArrayTotal;
        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(v.<Long>get("codigo"), filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(builder.lower(v.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getLocal())) {
            predicateList.add(builder.like(builder.lower(v.<String>get("local")),
                    "%" + filtro.getLocal().toLowerCase() + "%"));
        }
//        predicateList.add(builder.isNull(v.get("finalizouEm")));

        predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);
        criteriaQuery.select(v).where(predArray);
        PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);
        typedQuery = em.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        typedQuery.setHint("hibernate.query.passDistinctThrough", false);
        List<Evento> vacinas = typedQuery.getResultList();
        logger.info("Calculando o total de registros que o filtro retornará.");
        CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
        Root<Evento> vTotal = criteriaQueryTotal.from(Evento.class);
        criteriaQueryTotal.select(builder.count(vTotal));
        if (filtro.getCodigo() != null) {
            predicateListTotal.add(builder.equal(vTotal.<Long>get("codigo"), filtro.getCodigo()));
        }
        if (StringUtils.hasText(filtro.getNome())) {
            predicateListTotal.add(builder.like(builder.lower(vTotal.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getLocal())) {
            predicateListTotal.add(builder.like(builder.lower(vTotal.<String>get("local")),
                    "%" + filtro.getLocal().toLowerCase() + "%"));
        }
//        predicateListTotal.add(builder.isNull(v.get("finalizouEm")));

        predArrayTotal = new Predicate[predicateListTotal.size()];
        predicateListTotal.toArray(predArrayTotal);
        criteriaQueryTotal.where(predArrayTotal);
        TypedQuery<Long> typedQueryTotal = em.createQuery(criteriaQueryTotal);
        long totalVacinas = typedQueryTotal.getSingleResult();
        logger.info("O filtro retornará {} registros.", totalVacinas);
        Page<Evento> page = new PageImpl<>(vacinas, pageable, totalVacinas);
        return page;
    }
}
