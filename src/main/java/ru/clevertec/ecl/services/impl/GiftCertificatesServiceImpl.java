package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.GiftCertificatesRepository;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;
import ru.clevertec.ecl.dto.certificates.UpdateGiftCertificateDto;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.services.TagsService;
import ru.clevertec.ecl.utils.mappers.GiftCertificateMapper;
import ru.clevertec.ecl.utils.mappers.TagMapper;

import java.util.*;
import java.util.stream.Stream;

import static ru.clevertec.ecl.utils.PageableHelper.*;
import static ru.clevertec.ecl.utils.SpecificationHelper.*;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {
    private final GiftCertificatesRepository repository;
    private final GiftCertificateMapper mapper;
    private final TagsService tagsService;
    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesRepository repository,
                                       GiftCertificateMapper mapper,
                                       TagsService tagsService,
                                       TagMapper tagMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.tagsService = tagsService;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDto> getAllGiftCertificatesWithFiltering(FilterCriteria filter, Pageable pageable) {
        pageable = validateOnlyNameOrCreateDateOrders(pageable);
        Specification<GiftCertificate> specification = Specification.allOf(getSpecificationsFromFilter(filter));
        List<GiftCertificate> certificates = repository.findAll(specification, pageable).getContent();
        return mapper.convertGiftCertificatesToDtos(certificates);
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateDto getGiftCertificateById(long id) {
        Optional<GiftCertificate> certificate = repository.findById(id);
        if (certificate.isEmpty()) {
            throw new ItemNotFoundException("Gift certificate with ID " + id + " not found in database",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        return mapper.convertGiftCertificateToDto(certificate.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GiftCertificate> getGiftCertificateByIdWithoutTags(long id) {
        return repository.findWithoutTagsById(id);
    }

    @Override
    @Transactional
    public ModificationResponse addGiftCertificate(GiftCertificateDto dto) {
        GiftCertificate certificate = mapper.convertDtoToGiftCertificate(dto);
        if (checkGiftCertificateExist(certificate)) {
            throw new ItemExistException("Cannot add: gift certificate with similar name, description, price " +
                    "and duration already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
        Set<Tag> tags = tagsService.addAllTagsIfNotExist(certificate.getTags());
        certificate.setTags(tags);
        long generatedId = repository.save(certificate).getId();
        return new ModificationResponse(generatedId, "Gift certificate added successfully");
    }

    @Override
    @Transactional
    public ModificationResponse updateGiftCertificate(long id, UpdateGiftCertificateDto dto) {
        checkFieldsForUpdatingExist(dto);
        Optional<GiftCertificate> oCertificate = repository.findById(id);
        if (oCertificate.isEmpty()) {
            throw new ItemNotFoundException("Cannot update: gift certificate with ID " + id + " not found",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        Set<Tag> tagsFromDto = tagMapper.convertTagDtosToTags(dto.getTags());
        Set<Tag> tags = tagsService.addAllTagsIfNotExist(tagsFromDto);
        GiftCertificate certificate = oCertificate.get();
        mapper.updateGiftCertificateFields(dto, tags, certificate);
        if (checkGiftCertificateExist(certificate)) {
            throw new ItemExistException("Cannot update: gift certificate with similar name, description, price " +
                    "and duration already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
        repository.save(certificate);
        return new ModificationResponse(id, "Gift certificate updated successfully");

    }

    @Override
    @Transactional
    public ModificationResponse deleteGiftCertificateById(long id) {
        int deletedCount = repository.deleteById(id);
        if (deletedCount == 0) {
            throw new ItemNotFoundException("Cannot delete: gift certificate with ID " + id + " not found",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        return new ModificationResponse(id, "Gift certificate deleted successfully");
    }

    private void checkFieldsForUpdatingExist(UpdateGiftCertificateDto dto) {
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

    private boolean checkGiftCertificateExist(GiftCertificate certificate) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "createDate", "lastUpdateDate");
        Example<GiftCertificate> existExample = Example.of(certificate, matcher);
        return repository.exists(existExample);
    }
}
