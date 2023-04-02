package ru.clevertec.ecl.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
import ru.clevertec.ecl.models.*;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.SQLFilter;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.sql.PreparedStatement;
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
        SQLFilter sqlFilter = getFilterSQL(filterCriteria);
        sqlWithFilterAndSoring.append(sqlFilter.getSql());
        sqlWithFilterAndSoring.append(getSortingOrder(sortCriteria));
        return template.query(sqlWithFilterAndSoring.toString(), rs -> {
            Map<Long, GiftCertificateBuilder> giftCertificatesBuilders = new LinkedHashMap<>();
            GiftCertificateBuilder builder;
            while (rs.next()) {
                long certificateId = rs.getLong("gc_id");
                builder = giftCertificatesBuilders.get(certificateId);
                if (builder == null) {
                    builder = createBuilderWithValues(rs, certificateId);
                    giftCertificatesBuilders.put(certificateId, builder);
                }
                addTagToBuilder(rs, builder);
            }
            return giftCertificatesBuilders.values().stream()
                    .map(GiftCertificateBuilder::build)
                    .toList();
        }, sqlFilter.getFilteringFields().toArray());
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(long id) {
        return Optional.ofNullable(template.query(SELECT_GIFT_CERTIFICATE_WITH_TAGS_BY_ID, rs -> {
            GiftCertificate giftCertificate = null;
            GiftCertificateBuilder builder = null;
            while (rs.next()) {
                if (builder == null) {
                    long certificateId = rs.getLong("gc_id");
                    builder = createBuilderWithValues(rs, certificateId);
                }
                addTagToBuilder(rs, builder);
            }
            if (builder != null) {
                giftCertificate = builder.build();
            }
            return giftCertificate;
        }, id));
    }

    @Override
    public long addGiftCertificate(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_NEW_GIFT_CERTIFICATE, new String[]{"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setLong(3, giftCertificate.getPrice());
            ps.setLong(4, (int) giftCertificate.getDuration().toDays());
            Timestamp created = convertLocalDateTimeToTimestamp(LocalDateTime.now());
            ps.setTimestamp(5, created);
            ps.setTimestamp(6, created);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();

    }

    @Override
    public int updateGiftCertificate(long id, GiftCertificate certificate) {
        return template.update(UPDATE_GIFT_CERTIFICATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                (int) certificate.getDuration().toDays(),
                convertLocalDateTimeToTimestamp(certificate.getCreateDate()),
                convertLocalDateTimeToTimestamp(certificate.getLastUpdateDate()),
                id);
    }

    @Override
    public int deleteGiftCertificate(long id) {
        return template.update(DELETE_GIFT_CERTIFICATE_BY_ID, id);
    }

    private SQLFilter getFilterSQL(FilterCriteria filter) {
        List<Object> fieldsValues = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (filter != null) {
            if (filter.getTag() != null) {
                sb.append(FILTER_BY_TAG_NAME);
                fieldsValues.add(filter.getTag());
            }
            if (filter.getName() != null) {
                sb.append(getAndClauseIfAdditionalFilter(sb));
                sb.append(FILTER_BY_GIFT_CERTIFICATE_NAME);
                fieldsValues.add("%" + filter.getName() + "%");
            }
            if (filter.getDescription() != null) {
                sb.append(getAndClauseIfAdditionalFilter(sb));
                sb.append(FILTER_BY_GIFT_CERTIFICATE_DESCRIPTION);
                fieldsValues.add("%" + filter.getDescription() + "%");
            }
            if (!sb.isEmpty()) {
                sb.insert(0, " WHERE ");
            }
        }
        return new SQLFilter(sb.toString(), fieldsValues);
    }

    private String getAndClauseIfAdditionalFilter(StringBuilder sb) {
        return sb.isEmpty() ? "" : " AND ";
    }

    private String getSortingOrder(SortCriteria criteria) {
        String sortingOrder = "";
        if (criteria != null && criteria.getSort() != null && criteria.getSort().size() != 0) {
            String sorting = criteria.getSort().stream()
                    .map(this::getSplitParams)
                    .filter(this::isValidParams)
                    .map(this::getFieldAndItsOrder)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.joining(", "));
            if (!sorting.isBlank()) {
                sortingOrder = " ORDER BY " + sorting;
            }
        }
        return sortingOrder;
    }

    private List<String> getSplitParams(String params) {
        return List.of(params.split(":"));
    }

    private boolean isValidParams(List<String> params) {
        return params.size() > 0 && params.get(0).matches("[A-Za-z]+");
    }

    private String getFieldAndItsOrder(List<String> params) {
        StringBuilder sb = new StringBuilder();
        if ("name".equalsIgnoreCase(params.get(0))) {
            sb.append("gc_name");
        } else if ("date".equalsIgnoreCase(params.get(0))) {
            sb.append("create_date");
        }
        if (!sb.isEmpty() && params.size() > 1 && "desc".equalsIgnoreCase(params.get(1))) {
            sb.append(" DESC");
        }
        return sb.toString();
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
            builder.tag(tag);
        }
    }

    private Timestamp convertLocalDateTimeToTimestamp(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
