package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.List;

public interface GiftCertificatesService {
    List<GiftCertificateDto> getAllGiftCertificates(FilterCriteria filter, SortCriteria sorting, PaginationCriteria pagination);
    GiftCertificateDto getGiftCertificateById(long id);
    ModificationResponse addGiftCertificate(GiftCertificateDto dto);
    ModificationResponse updateGiftCertificate(long id, UpdateGiftCertificateDto dto);
    ModificationResponse deleteGiftCertificate(long id);
}
