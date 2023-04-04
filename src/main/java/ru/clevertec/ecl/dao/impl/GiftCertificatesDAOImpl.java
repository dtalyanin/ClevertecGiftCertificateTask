package ru.clevertec.ecl.dao.impl;

//import jakarta.persistence.NoResultException;
//import jakarta.persistence.Query;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
import ru.clevertec.ecl.models.*;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.SQLFilter;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.models.GiftCertificate.*;
import static ru.clevertec.ecl.utils.constants.GiftCertificatesSQL.*;

@Repository
public class GiftCertificatesDAOImpl implements GiftCertificatesDAO {

    private final JdbcTemplate template;
    private final SessionFactory factory;

    @Autowired
    public GiftCertificatesDAOImpl(SessionFactory factory) {
        this.template = null;
        this.factory = factory;
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates(FilterCriteria filterCriteria, SortCriteria sortCriteria) {
        StringBuilder sqlWithFilterAndSoring = new StringBuilder(SELECT_ALL_GIFT_CERTIFICATES_WITH_TAGS);
        CriteriaBuilder cb = factory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cr = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> r = cr.from(GiftCertificate.class);
        Fetch<GiftCertificate, Tag> fetch = r.fetch("tags", JoinType.LEFT);
        List<Order> orders = getSortingOrder(sortCriteria, cb, r);
        Predicate[] predicates = getFilterSQL(filterCriteria, cb, r);
        cr.select(r).where(predicates);
        cr.orderBy(orders);
// here join with the root instead of the fetch
// casting the fetch to the join could cause portability problems
// plus, not nice

//        sqlWithFilterAndSoring.append(sqlFilter.getSql());
//        sqlWithFilterAndSoring.append(getSortingOrder(sortCriteria, cb));

//        return factory.getCurrentSession()
//                .createQuery("SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags", GiftCertificate.class)
//                .list();
        return factory.getCurrentSession().createQuery(cr).list();
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(long id) {
        Query query = factory.getCurrentSession()
                .createQuery("SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags WHERE gc.id = :id",
                        GiftCertificate.class)
                .setParameter("id", id);
        GiftCertificate giftCertificate;
        try {
            giftCertificate = (GiftCertificate) query.getSingleResult();
        } catch (NoResultException e) {
            giftCertificate = null;
        }
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public long addGiftCertificate(GiftCertificate giftCertificate) {
        factory.getCurrentSession().persist(giftCertificate);
        return giftCertificate.getId();
    }

    @Override
    public int updateGiftCertificate(long id, GiftCertificate certificate) {
        factory.getCurrentSession().merge(certificate);
        return 1;
    }

    @Override
    public int deleteGiftCertificate(long id) {
        return factory.getCurrentSession()
                .createMutationQuery("DELETE GiftCertificate gc WHERE gc.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    private Predicate[] getFilterSQL(FilterCriteria filter, CriteriaBuilder cb, Root<GiftCertificate> r) {
        List<Predicate> fieldsValues = new ArrayList<>();
        if (filter != null) {
            if (filter.getTag() != null) {
                fieldsValues.add(cb.equal(r.get("tags").get("name"), filter.getTag()));
            }
            if (filter.getName() != null) {
                fieldsValues.add(cb.like(cb.lower(r.get("name")), '%' + filter.getName().toLowerCase() + '%'));
            }
            if (filter.getDescription() != null) {
                fieldsValues.add(cb.like(cb.lower(r.get("description")), '%' + filter.getDescription().toLowerCase() + '%'));
            }
        }
        return fieldsValues.toArray(new Predicate[fieldsValues.size()]);
    }

    private List<Order> getSortingOrder(SortCriteria criteria, CriteriaBuilder cb, Root<GiftCertificate> r) {
        List<Order> sortingOrder = new ArrayList<>();
        if (criteria != null && criteria.getSort() != null && criteria.getSort().size() != 0) {
            sortingOrder = criteria.getSort().stream()
                    .map(this::getSplitParams)
                    .filter(this::isValidParams)
                    .map(params -> getFieldAndItsOrder(params, cb, r)).toList();
        }
        return sortingOrder;
    }

    private List<String> getSplitParams(String params) {
        return List.of(params.split(":"));
    }

    private boolean isValidParams(List<String> params) {
        return params.size() > 0 && params.get(0).matches("[A-Za-z]+");
    }

    private Order getFieldAndItsOrder(List<String> params, CriteriaBuilder cb, Root<GiftCertificate> r) {
        Order order;
        Path<?> path = null;
        if ("name".equalsIgnoreCase(params.get(0))) {
            path = r.get("name");
        } else if ("date".equalsIgnoreCase(params.get(0))) {
            path = r.get("createDate");
        }
        if (path != null && params.size() > 1 && "desc".equalsIgnoreCase(params.get(1))) {
            order = cb.desc(path);
        } else {
            order = cb.asc(path);
        }
        return order;
    }

    private GiftCertificateBuilder createBuilderWithValues(ResultSet rs, long id) throws SQLException {
        GiftCertificateBuilder builder = builder();
        builder.id(id)
                .name(rs.getString("gc_name"))
                .description(rs.getString("description"))
                .price(rs.getLong("price"))
                .duration(Duration.ofDays(rs.getInt("duration")))
                .createDate(rs.getTimestamp("create_date").toLocalDateTime())
                .lastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime());
        return builder;
    }

    private void addTagToBuilder(ResultSet rs, GiftCertificateBuilder builder) throws SQLException {
        long tagId = rs.getLong("t_id");
        if (tagId > 0) {
            Tag tag = new Tag();
            tag.setId(tagId);
            tag.setName(rs.getString("t_name"));
        }
    }

    private Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
