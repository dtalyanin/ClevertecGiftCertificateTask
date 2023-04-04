package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.List;
import java.util.Optional;

public interface GiftCertificatesDAO {
    List<GiftCertificate> getAllGiftCertificates(FilterCriteria filterCriteria, SortCriteria sortCriteria, PaginationCriteria pagination);
    Optional<GiftCertificate> getGiftCertificateById(long id);
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate);
    GiftCertificate updateGiftCertificate(long id, GiftCertificate giftCertificate);
    boolean deleteGiftCertificate(long id);
}
