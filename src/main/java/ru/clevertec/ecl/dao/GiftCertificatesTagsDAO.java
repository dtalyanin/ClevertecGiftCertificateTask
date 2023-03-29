package ru.clevertec.ecl.dao;

import java.util.List;

public interface GiftCertificatesTagsDAO {
    int addGiftCertificateTags(long certificateId, List<Long> tags);
    int deleteGiftCertificateTags(long certificateId);
}
