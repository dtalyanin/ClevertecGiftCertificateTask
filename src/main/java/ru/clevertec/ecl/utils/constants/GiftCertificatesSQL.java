package ru.clevertec.ecl.utils.constants;

public class GiftCertificatesSQL {
    public static final String SELECT_ALL_GIFT_CERTIFICATES_WITH_TAGS = "SELECT gc FROM GiftCertificate " +
            "gc LEFT JOIN FETCH gc.tags";
    public static final String SELECT_GIFT_CERTIFICATE_WITH_TAGS_BY_ID =
            "SELECT gc FROM GiftCertificate gc LEFT JOIN FETCH gc.tags WHERE gc.id = :id";
    public static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE GiftCertificate gc WHERE gc.id = :id";
    public static final String FILTER_BY_TAG_NAME = "gc.id = SOME (SELECT gc.id FROM gc.tags t WHERE t.name = :tag)";
    public static final String FILTER_BY_GIFT_CERTIFICATE_NAME = "lower(gc.name) LIKE lower(:name)";
    public static final String FILTER_BY_GIFT_CERTIFICATE_DESCRIPTION = "lower(gc.description) LIKE lower(:description)";
}
