package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;

import java.util.List;
import java.util.Optional;

public interface GiftCertificatesService {
    List<GiftCertificateDto> getAllGiftCertificatesWithFiltering(FilterCriteria filter, Pageable pageable);
    GiftCertificateDto getGiftCertificateById(long id);
    Optional<GiftCertificate> getGiftCertificateByIdWithoutTags(long id);
    ModificationResponse addGiftCertificate(GiftCertificateDto dto);
    ModificationResponse updateGiftCertificate(long id, UpdateGiftCertificateDto dto);
    ModificationResponse deleteGiftCertificateById(long id);
}
