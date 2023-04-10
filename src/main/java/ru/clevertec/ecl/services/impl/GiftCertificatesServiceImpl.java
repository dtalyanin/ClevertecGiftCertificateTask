package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.impl.GiftCertificatesRepository;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.criteries.SortCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.services.TagsService;
import ru.clevertec.ecl.utils.mappers.GiftCertificateMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {
    private final GiftCertificatesRepository repository;
    private final GiftCertificateMapper mapper;
    private final TagsService tagsService;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesRepository repository,
                                       GiftCertificateMapper mapper,
                                       TagsService tagsService) {
        this.repository = repository;
        this.mapper = mapper;
        this.tagsService = tagsService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDTO> getAllGiftCertificates(FilterCriteria filter, SortCriteria sorting,
                                                           PaginationCriteria pagination) {
        List<GiftCertificate> giftCertificates = repository.findAll();
        return mapper.allGiftCertificateToDTO(giftCertificates);
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateDTO getGiftCertificateById(long id) {
        Optional<GiftCertificate> certificate = repository.findById(id);
        if (certificate.isEmpty()) {
            throw new ItemNotFoundException("Gift certificate with ID " + id + " not found in database",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        return mapper.giftCertificateToDTO(certificate.get());
    }

    @Override
    @Transactional
    public ModificationResponse addGiftCertificate(GiftCertificateDTO dto) {
        GiftCertificate certificate = mapper.dtoToGiftCertificate(dto);
        if (checkGiftCertificateExist(certificate)) {
            throw new ItemExistException("Cannot update: gift certificate with similar name, description, price " +
                    "and duration already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
        Set<Tag> tags = tagsService.addAllTagsIfNotExist(certificate.getTags());
        certificate.setTags(tags);
        repository.save(certificate);
        return new ModificationResponse(certificate.getId(), "Gift certificate added successfully");
    }

    private boolean checkGiftCertificateExist(GiftCertificate certificate) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "lastUpdateDate", "createdDate");
        Example<GiftCertificate> existExample = Example.of(certificate, matcher);
        return repository.exists(existExample);
    }

    @Override
    @Transactional
    public ModificationResponse updateGiftCertificate(long id, ModGiftCertificateDTO dto) {
        checkFieldsForUpdating(dto);
        if (repository.existsByNameAndDescriptionAndDurationAndPrice(dto.getName(),
                dto.getDescription(), mapper.convertDaysToDuration(dto.getDuration()),
                mapper.convertPriceFromRublesToPriceInCoins(dto.getPrice()))) {
            throw new ItemExistException("Cannot update: gift certificate with similar name, description, price and duration " +
                    "already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
        Optional<GiftCertificate> oCertificate = repository.findById(id);
        if (oCertificate.isEmpty()) {
            throw new ItemNotFoundException("Cannot update: gift certificate with ID " + id + " not found",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        GiftCertificate certificate = mapper.modDTOToGiftCertificate(dto, oCertificate.get());
        Set<Tag> tags = tagsService.addAllTagsIfNotExist(certificate.getTags());
        certificate.setTags(tags);
        return new ModificationResponse(id, "Gift certificate updated successfully");

    }

    @Override
    @Transactional
    public ModificationResponse deleteGiftCertificate(long id) {
        int deletedCount = repository.deleteById(id);
        if (deletedCount == 0) {
            throw new ItemNotFoundException("Cannot delete: gift certificate with ID " + id + " not found",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        return new ModificationResponse(id, "Gift certificate deleted successfully");
    }

    private void checkFieldsForUpdating(ModGiftCertificateDTO dto) {
        boolean fieldsForUpdatingNotExist = Stream.of(
                        dto.getName(),
                        dto.getDescription(),
                        dto.getTags(),
                        dto.getPrice(),
                        dto.getDuration())
                .allMatch(Objects::isNull);
        if (fieldsForUpdatingNotExist) {
            throw new EmptyItemException("Cannot update: no fields to update",
                    ErrorCode.NO_CERTIFICATE_FIELDS_FOR_UPDATE);
        }
    }
}
