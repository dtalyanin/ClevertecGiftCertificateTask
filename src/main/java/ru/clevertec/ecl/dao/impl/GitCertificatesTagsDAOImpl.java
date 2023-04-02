package ru.clevertec.ecl.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.GiftCertificatesTagsDAO;

import java.util.Arrays;
import java.util.List;

import static ru.clevertec.ecl.utils.constants.GiftCertificatesTagsSQL.*;

@Repository
public class GitCertificatesTagsDAOImpl implements GiftCertificatesTagsDAO {
    private final JdbcTemplate template;

    public GitCertificatesTagsDAOImpl() {
        this.template = null;
    }

    @Override
    public int addGiftCertificateTags(long certificateId, List<Long> tagsIds) {
        List<Object[]> values = tagsIds.stream()
                .map(tagId -> new Object[] {certificateId, tagId})
                .toList();
        int[] addedRows = template.batchUpdate(ADD_GIFT_CERTIFICATE_TAGS, values);
        return Arrays.stream(addedRows).sum();
    }

    @Override
    public int deleteGiftCertificateTags(long certificateId) {
        return template.update(DELETE_GIFT_CERTIFICATE_TAGS, certificateId);
    }
}
