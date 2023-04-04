package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.List;

public interface GiftCertificatesService {
    List<GiftCertificateDTO> getAllGiftCertificates(FilterCriteria filter, SortCriteria sorting, PaginationCriteria pagination);
    GiftCertificateDTO getGiftCertificateById(long id);
    ModificationResponse addGiftCertificate(GiftCertificateDTO dto);
    ModificationResponse updateGiftCertificate(long id, ModGiftCertificateDTO dto);
    ModificationResponse deleteGiftCertificate(long id);
}
