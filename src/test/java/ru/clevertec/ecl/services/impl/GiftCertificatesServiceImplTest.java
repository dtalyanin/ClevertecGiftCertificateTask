package ru.clevertec.ecl.services.impl;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
import ru.clevertec.ecl.dao.GiftCertificatesTagsDAO;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.SortCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.TagsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static generators.factories.GiftCertificateDTOFactory.*;
import static generators.factories.GiftCertificateFactory.getSimpleGiftCertificate;
import static generators.factories.GiftCertificateFactory.getSimpleGiftCertificates;
import static generators.factories.ModGiftCertificateDTOFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificatesServiceImplTest {

    @Mock
    private GiftCertificatesDAO dao;
    @Mock
    private TagsService tagsService;
    @Mock
    private GiftCertificatesTagsDAO certificatesTagsDAO;
    @InjectMocks
    @Autowired
    private GiftCertificatesServiceImpl service;

    @Test
    void getAllGiftCertificatesShouldReturnListOfDTOs() {
        when(dao.getAllGiftCertificates(any(FilterCriteria.class), any(SortCriteria.class)))
                .thenReturn(getSimpleGiftCertificates());
        List<GiftCertificateDTO> actual = service.getAllGiftCertificates(new FilterCriteria(), new SortCriteria());
        List<GiftCertificateDTO> expected = getSimpleGiftCertificateDTOs();
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1))
                .getAllGiftCertificates(any(FilterCriteria.class), any(SortCriteria.class));
    }

    @Test
    void getAllGiftCertificatesShouldReturnEmptyList() {
        when(dao.getAllGiftCertificates(any(FilterCriteria.class), any(SortCriteria.class)))
                .thenReturn(Collections.emptyList());
        List<GiftCertificateDTO> actual = service.getAllGiftCertificates(new FilterCriteria(), new SortCriteria());
        List<GiftCertificateDTO> expected = Collections.emptyList();
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1))
                .getAllGiftCertificates(any(FilterCriteria.class), any(SortCriteria.class));
    }

    @Test
    void checkGetGiftCertificateByIdShouldReturnCertificate() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        when(dao.getGiftCertificateById(1L)).thenReturn(Optional.of(certificate));
        GiftCertificateDTO actual = service.getGiftCertificateById(1L);
        GiftCertificateDTO expected = getSimpleGiftCertificateDTO();
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).getGiftCertificateById(anyLong());
    }

    @Test
    void checkGetGiftCertificateByIdShouldThrowExceptionNotFoundId() {
        when(dao.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getGiftCertificateById(1))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Gift certificate with ID 1 not found in database");
        verify(dao, times(1)).getGiftCertificateById(anyLong());
    }

    @Test
    void checkAddGiftCertificateShouldReturnResponseWithGeneratedId() {
        GiftCertificateDTO certificateDTO = getSimpleGiftCertificateDTO();
        when(dao.addGiftCertificate(any(GiftCertificate.class))).thenReturn(1L);
        when(tagsService.addAllTagsIfNotExist(anyList())).thenReturn(Collections.emptyList());
        ModificationResponse actual = service.addGiftCertificate(certificateDTO);
        ModificationResponse expected = new ModificationResponse(1L, "Gift certificate added successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).addGiftCertificate(any(GiftCertificate.class));
        verify(tagsService, times(1)).addAllTagsIfNotExist(anyList());
        verify(certificatesTagsDAO, times(1)).addGiftCertificateTags(anyLong(), anyList());
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionInvalidDTO() {
        GiftCertificateDTO certificateDTO = getGiftCertificateDTOWithoutFields();
        when(dao.addGiftCertificate(any(GiftCertificate.class))).thenThrow(new DuplicateKeyException("Message"));
        assertThatThrownBy(() -> service.addGiftCertificate(certificateDTO))
                .isInstanceOf(ConstraintViolationException.class);
        verify(dao, times(0)).addGiftCertificate(any(GiftCertificate.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anyList());
        verify(certificatesTagsDAO, times(0)).addGiftCertificateTags(anyLong(), anyList());
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionCertificateExist() {
        GiftCertificateDTO certificateDTO = getSimpleGiftCertificateDTO();
        when(dao.addGiftCertificate(any(GiftCertificate.class))).thenThrow(new DuplicateKeyException("Message"));
        assertThatThrownBy(() -> service.addGiftCertificate(certificateDTO))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: gift certificate with similar parameters already exist in database");
        verify(dao, times(1)).addGiftCertificate(any(GiftCertificate.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anyList());
        verify(certificatesTagsDAO, times(0)).addGiftCertificateTags(anyLong(), anyList());
    }

    @Test
    void checkUpdateGiftCertificateShouldReturnResponseWithUpdatedId() {
        ModGiftCertificateDTO modCertificateDTO = getSimpleModGiftCertificateDTO();
                when(dao.getGiftCertificateById(5L)).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(dao.updateGiftCertificate(anyLong(), any(GiftCertificate.class))).thenReturn(1);
        when(tagsService.addAllTagsIfNotExist(anyList())).thenReturn(Collections.emptyList());
        ModificationResponse actual = service.updateGiftCertificate(5L, modCertificateDTO);
        ModificationResponse expected = new ModificationResponse(5L, "Gift certificate updated successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
        verify(dao, times(1)).getGiftCertificateById(anyLong());
        verify(tagsService, times(1)).addAllTagsIfNotExist(anyList());
        verify(certificatesTagsDAO, times(1)).deleteGiftCertificateTags(anyLong());
        verify(certificatesTagsDAO, times(1)).addGiftCertificateTags(anyLong(), anyList());
    }

    @Test
    void checkUpdateGiftCertificateShouldReturnResponseWithUpdatedIdIfNoTagsForUpdate() {
        ModGiftCertificateDTO modCertificateDTO = getModGiftCertificateDTOWithOnlyNamePriceDuration();
                when(dao.getGiftCertificateById(5L)).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(dao.updateGiftCertificate(anyLong(), any(GiftCertificate.class))).thenReturn(1);
        ModificationResponse actual = service.updateGiftCertificate(5L, modCertificateDTO);
        ModificationResponse expected = new ModificationResponse(5L, "Gift certificate updated successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
        verify(dao, times(1)).getGiftCertificateById(anyLong());
        verify(tagsService, times(0)).addAllTagsIfNotExist(anyList());
        verify(certificatesTagsDAO, times(0)).deleteGiftCertificateTags(anyLong());
        verify(certificatesTagsDAO, times(0)).addGiftCertificateTags(anyLong(), anyList());
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionIdNotFound() {
        when(dao.getGiftCertificateById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getSimpleModGiftCertificateDTO()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot update: gift certificate with ID 1 not found");
        verify(dao, times(0)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
        verify(dao, times(1)).getGiftCertificateById(anyLong());
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionNoFieldsToUpdate() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getModGiftCertificateDTOWithoutFields()))
                .isInstanceOf(EmptyItemException.class)
                .hasMessage("Cannot update: no fields to update");
        verify(dao, times(0)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionCertificateExist() {
        ModGiftCertificateDTO modCertificateDTO = getSimpleModGiftCertificateDTO();
        when(dao.getGiftCertificateById(1L)).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(dao.updateGiftCertificate(anyLong(), any(GiftCertificate.class)))
                .thenThrow(new DuplicateKeyException("Message"));
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, modCertificateDTO))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: gift certificate with similar parameters already exist in database");
        verify(dao, times(1)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
        verify(dao, times(1)).getGiftCertificateById(anyLong());
        verify(tagsService, times(0)).addAllTagsIfNotExist(anyList());
        verify(certificatesTagsDAO, times(0)).deleteGiftCertificateTags(anyLong());
        verify(certificatesTagsDAO, times(0)).addGiftCertificateTags(anyLong(), anyList());
    }

    @Test
    void checkDeleteGiftCertificateShouldReturnResponseWithDeletedId() {
        when(dao.deleteGiftCertificate(anyLong())).thenReturn(1);
        ModificationResponse actual = service.deleteGiftCertificate(5L);
        ModificationResponse expected = new ModificationResponse(5L, "Gift certificate deleted successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).deleteGiftCertificate(anyLong());
    }

    @Test
    void checkDeleteGiftCertificateShouldThrowExceptionNotFound() {
        when(dao.deleteGiftCertificate(anyLong())).thenReturn(0);
        assertThatThrownBy(() -> service.deleteGiftCertificate(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: gift certificate with ID 1 not found");
        verify(dao, times(1)).deleteGiftCertificate(anyLong());
    }
}