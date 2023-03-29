package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.List;
import java.util.Optional;

public interface GiftCertificatesDAO {
    List<GiftCertificate> getAllGiftCertificates(FilterCriteria filterCriteria, SortCriteria sortCriteria);
    Optional<GiftCertificate> getGiftCertificateById(long id);
    long addGiftCertificate(GiftCertificate giftCertificate);
    int updateGiftCertificate(long id, GiftCertificate giftCertificate);
    int deleteGiftCertificate(long id);
}
