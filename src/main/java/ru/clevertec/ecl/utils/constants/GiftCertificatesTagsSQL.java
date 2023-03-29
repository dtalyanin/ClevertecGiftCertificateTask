package ru.clevertec.ecl.utils.constants;

public class GiftCertificatesTagsSQL {
    public static final String ADD_GIFT_CERTIFICATE_TAGS = "INSERT INTO gift_certificates_tags " +
            "(certificate_id, tag_id) VALUES (?, ?)";
    public static final String DELETE_GIFT_CERTIFICATE_TAGS = "DELETE FROM gift_certificates_tags " +
            "WHERE certificate_id = ?";
}
