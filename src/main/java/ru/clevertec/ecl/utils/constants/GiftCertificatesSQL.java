package ru.clevertec.ecl.utils.constants;

public class GiftCertificatesSQL {
    public static final String SELECT_ALL_GIFT_CERTIFICATES_WITH_TAGS = "SELECT " +
            "gc.id AS gc_id, gc.name AS gc_name, description, price, duration, " +
            "create_date, last_update_date, t.id AS t_id, t.name AS t_name " +
            "FROM gift_certificates gc " +
            "LEFT JOIN gift_certificates_tags gct ON gc.id = gct.certificate_id " +
            "LEFT JOIN tags t on t.id = gct.tag_id";
    public static final String SELECT_GIFT_CERTIFICATE_WITH_TAGS_BY_ID = "SELECT " +
            "gc.id AS gc_id, gc.name AS gc_name, description, price, duration, " +
            "create_date, last_update_date, t.id AS t_id, t.name AS t_name " +
            "FROM gift_certificates gc " +
            "LEFT JOIN gift_certificates_tags gct ON gc.id = gct.certificate_id " +
            "LEFT JOIN tags t on t.id = gct.tag_id " +
            "WHERE gc.id = ?";
    public static final String INSERT_NEW_GIFT_CERTIFICATE = "INSERT INTO gift_certificates " +
            "(name, description, price, duration, create_date, last_update_date) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificates " +
            "SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, " +
            "last_update_date = ? " +
            "WHERE id = ?";
    public static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificates WHERE id = ?";
    public static final String FILTER_BY_TAG_NAME = "gc.id IN (SELECT certificate_id FROM tags t " +
            "INNER JOIN gift_certificates_tags gct ON t.id = gct.tag_id WHERE name ILIKE ?)";
    public static final String FILTER_BY_GIFT_CERTIFICATE_NAME = "gc.name ILIKE ?";
    public static final String FILTER_BY_GIFT_CERTIFICATE_DESCRIPTION = "description ILIKE ?";
}
