package ru.clevertec.ecl.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
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
    private final GiftCertificatesDAO dao;
    private final GiftCertificateMapper mapper;
    private final TagsService tagsService;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesDAO dao, GiftCertificateMapper mapper, TagsService tagsService) {
        this.dao = dao;
        this.mapper = mapper;
        this.tagsService = tagsService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDTO> getAllGiftCertificates(FilterCriteria filter, SortCriteria sorting, PaginationCriteria pagination) {
        List<GiftCertificate> giftCertificates = dao.getAllGiftCertificates(filter, sorting, pagination);
        return mapper.allGiftCertificateToDTO(giftCertificates);
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateDTO getGiftCertificateById(long id) {
        Optional<GiftCertificate> certificate = dao.getGiftCertificateById(id);
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
        try {
            Set<Tag> tags = tagsService.addAllTagsIfNotExist(certificate.getTags());
            certificate.setTags(tags);
            LocalDateTime now = LocalDateTime.now();
            certificate.setCreateDate(now);
            certificate.setLastUpdateDate(now);
            dao.addGiftCertificate(certificate);
            return new ModificationResponse(certificate.getId(), "Gift certificate added successfully");
        } catch (ConstraintViolationException e) {
            throw new ItemExistException("Cannot add: gift certificate with similar name, description, price and duration " +
                    "already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
    }

    @Override
    @Transactional
    public ModificationResponse updateGiftCertificate(long id, ModGiftCertificateDTO dto) {
        checkFieldsForUpdating(dto);
        Optional<GiftCertificate> oCertificate = dao.getGiftCertificateById(id);
        if (oCertificate.isEmpty()) {
            throw new ItemNotFoundException("Cannot update: gift certificate with ID " + id + " not found",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        GiftCertificate certificate = mapper.modDTOToGiftCertificate(dto, oCertificate.get());
        Set<Tag> tags = tagsService.addAllTagsIfNotExist(certificate.getTags());
        certificate.setTags(tags);
        certificate.setLastUpdateDate(LocalDateTime.now());
        try {
            System.out.println(2);
            dao.updateGiftCertificate(id, certificate);
            return new ModificationResponse(id, "Gift certificate updated successfully");
        } catch (Exception e) {
            System.out.println(e.getClass());
            throw new ItemExistException("Cannot update: gift certificate with similar " +
                    "parameters already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
    }

    @Override
    @Transactional
    public ModificationResponse deleteGiftCertificate(long id) {
        boolean certificateDeleted = dao.deleteGiftCertificate(id);
        if (!certificateDeleted) {
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
