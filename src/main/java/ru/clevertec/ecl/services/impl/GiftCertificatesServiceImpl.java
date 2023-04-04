package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
import ru.clevertec.ecl.dao.GiftCertificatesTagsDAO;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.*;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.SortCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.services.TagsService;
import ru.clevertec.ecl.utils.mappers.GiftCertificateMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class GiftCertificatesServiceImpl implements GiftCertificatesService {
    private final GiftCertificatesDAO dao;
    private final GiftCertificateMapper mapper;
    private final TagsService tagsService;
    private final GiftCertificatesTagsDAO certificatesTagsDAO;

    @Autowired
    public GiftCertificatesServiceImpl(GiftCertificatesDAO dao,
                                       GiftCertificateMapper mapper,
                                       TagsService tagsService,
                                       GiftCertificatesTagsDAO certificatesTagsDAO) {
        this.dao = dao;
        this.mapper = mapper;
        this.tagsService = tagsService;
        this.certificatesTagsDAO = certificatesTagsDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDTO> getAllGiftCertificates(FilterCriteria filter, SortCriteria sort) {
        List<GiftCertificate> giftCertificates = dao.getAllGiftCertificates(filter, sort);
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
            LocalDateTime now = LocalDateTime.now();
            certificate.setTags(tagsService.addAllTagsIfNotExist(certificate.getTags()));
            certificate.setCreateDate(now);
            certificate.setLastUpdateDate(now);
            long generatedId = dao.addGiftCertificate(certificate);
            return new ModificationResponse(generatedId, "Gift certificate added successfully");
        } catch (DuplicateKeyException e) {
            throw new ItemExistException("Cannot add: gift certificate with similar " +
                    "parameters already exist in database", ErrorCode.CERTIFICATE_EXIST);
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
        Set<Tag> existingTagsBefore = oCertificate.get().getTags();
        System.out.println(existingTagsBefore);
        GiftCertificate certificate = mapper.modDTOToGiftCertificate(dto, oCertificate.get());
        certificate.setTags(tagsService.addAllTagsIfNotExist(certificate.getTags()));
        certificate.setLastUpdateDate(LocalDateTime.now());
        try {
            dao.updateGiftCertificate(id, certificate);
//            if (dto.getTags() != null) {
//                List<Long> tagsIds = tagsService.addAllTagsIfNotExist(certificate.getTags());
//                certificatesTagsDAO.deleteGiftCertificateTags(id);
//                certificatesTagsDAO.addGiftCertificateTags(id, tagsIds);
//            }
            return new ModificationResponse(id, "Gift certificate updated successfully");
        } catch (DuplicateKeyException e) {
            throw new ItemExistException("Cannot update: gift certificate with similar " +
                    "parameters already exist in database", ErrorCode.CERTIFICATE_EXIST);
        }
    }

    @Override
    @Transactional
    public ModificationResponse deleteGiftCertificate(long id) {
        long deletedRows = dao.deleteGiftCertificate(id);
        if (deletedRows == 0) {
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
