package ru.clevertec.ecl.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.InvalidItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.criteries.SortCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.TagsService;
import ru.clevertec.ecl.utils.mappers.GiftCertificateMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static generators.factories.FilterCriteriaFactory.getFilterByTag;
import static generators.factories.GiftCertificateDTOFactory.getSimpleGiftCertificateDTO;
import static generators.factories.GiftCertificateDTOFactory.getSimpleGiftCertificateDTOs;
import static generators.factories.GiftCertificateFactory.getSimpleGiftCertificate;
import static generators.factories.GiftCertificateFactory.getSimpleGiftCertificates;
import static generators.factories.ModGiftCertificateDTOFactory.getModGiftCertificateDTOWithoutFields;
import static generators.factories.ModGiftCertificateDTOFactory.getSimpleModGiftCertificateDTO;
import static generators.factories.PaginationCriteriaFactory.getPaginationFrom0To10;
import static generators.factories.SortCriteriaFactory.getSortByName;
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
    private GiftCertificateMapper mapper;
    @InjectMocks
    private GiftCertificatesServiceImpl service;

    @Test
    void getAllGiftCertificatesShouldReturnListOfDTOs() {
        when(dao.getAllGiftCertificates(any(FilterCriteria.class), any(SortCriteria.class),
                any(PaginationCriteria.class))).thenReturn(getSimpleGiftCertificates());
        when(mapper.allGiftCertificateToDTO(anyList())).thenReturn(getSimpleGiftCertificateDTOs());

        List<GiftCertificateDTO> actual = service.getAllGiftCertificates(getFilterByTag(), getSortByName(),
                getPaginationFrom0To10());
        List<GiftCertificateDTO> expected = getSimpleGiftCertificateDTOs();

        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).getAllGiftCertificates(any(FilterCriteria.class),
                any(SortCriteria.class), any(PaginationCriteria.class));
    }

    @Test
    void getAllGiftCertificatesShouldReturnEmptyList() {
        when(dao.getAllGiftCertificates(any(FilterCriteria.class), any(SortCriteria.class),
                any(PaginationCriteria.class))).thenReturn(Collections.emptyList());
        when(mapper.allGiftCertificateToDTO(anyList())).thenReturn(Collections.emptyList());

        List<GiftCertificateDTO> actual = service.getAllGiftCertificates(getFilterByTag(), getSortByName(),
                getPaginationFrom0To10());
        List<GiftCertificateDTO> expected = Collections.emptyList();

        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).getAllGiftCertificates(any(FilterCriteria.class),
                any(SortCriteria.class), any(PaginationCriteria.class));
    }

    @Test
    void checkGetGiftCertificateByIdShouldReturnCertificate() {
        when(dao.getGiftCertificateById(1L)).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(mapper.giftCertificateToDTO(any(GiftCertificate.class))).thenReturn(getSimpleGiftCertificateDTO());

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
        when(dao.addGiftCertificate(any(GiftCertificate.class))).thenReturn(getSimpleGiftCertificate());
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());
        when(mapper.dtoToGiftCertificate(any(GiftCertificateDTO.class))).thenReturn(getSimpleGiftCertificate());

        ModificationResponse actual = service.addGiftCertificate(getSimpleGiftCertificateDTO());
        ModificationResponse expected = new ModificationResponse(1L, "Gift certificate added successfully");

        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).addGiftCertificate(any(GiftCertificate.class));
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionInvalidDTO() {
        when(mapper.dtoToGiftCertificate(any(GiftCertificateDTO.class)))
                .thenThrow(new InvalidItemException("Message", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE));

        assertThatThrownBy(() -> service.addGiftCertificate(getSimpleGiftCertificateDTO()))
                .isInstanceOf(InvalidItemException.class);
        verify(dao, times(0)).addGiftCertificate(any(GiftCertificate.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionCertificateExist() {
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());
        when(mapper.dtoToGiftCertificate(any(GiftCertificateDTO.class))).thenReturn(getSimpleGiftCertificate());
        when(dao.addGiftCertificate(any(GiftCertificate.class)))
                .thenThrow(new ConstraintViolationException("Message", null, null));

        assertThatThrownBy(() -> service.addGiftCertificate(getSimpleGiftCertificateDTO()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: gift certificate with similar name, description, price and duration " +
                        "already exist in database");
        verify(dao, times(1)).addGiftCertificate(any(GiftCertificate.class));
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkUpdateGiftCertificateShouldReturnResponseWithUpdatedId() {
        when(dao.getGiftCertificateById(5L)).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(mapper.modDTOToGiftCertificate(any(ModGiftCertificateDTO.class), any(GiftCertificate.class)))
                .thenReturn(getSimpleGiftCertificate());
        when(dao.updateGiftCertificate(anyLong(), any(GiftCertificate.class))).thenReturn(getSimpleGiftCertificate());
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());

        ModificationResponse actual = service.updateGiftCertificate(5L, getSimpleModGiftCertificateDTO());
        ModificationResponse expected = new ModificationResponse(5L, "Gift certificate updated successfully");

        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
        verify(dao, times(1)).getGiftCertificateById(anyLong());
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
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
        when(dao.getGiftCertificateById(1L)).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(mapper.modDTOToGiftCertificate(any(ModGiftCertificateDTO.class), any(GiftCertificate.class)))
                .thenReturn(getSimpleGiftCertificate());
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());
        when(dao.updateGiftCertificate(anyLong(), any(GiftCertificate.class)))
                .thenThrow(new ConstraintViolationException("Message", null, null));

        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getSimpleModGiftCertificateDTO()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: gift certificate with similar name, description, price and duration " +
                        "already exist in database");
        verify(dao, times(1)).updateGiftCertificate(anyLong(), any(GiftCertificate.class));
        verify(dao, times(1)).getGiftCertificateById(anyLong());
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkDeleteGiftCertificateShouldReturnResponseWithDeletedId() {
        when(dao.deleteGiftCertificate(anyLong())).thenReturn(true);

        ModificationResponse actual = service.deleteGiftCertificate(5L);
        ModificationResponse expected = new ModificationResponse(5L, "Gift certificate deleted successfully");

        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).deleteGiftCertificate(anyLong());
    }

    @Test
    void checkDeleteGiftCertificateShouldThrowExceptionNotFound() {
        when(dao.deleteGiftCertificate(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> service.deleteGiftCertificate(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: gift certificate with ID 1 not found");
        verify(dao, times(1)).deleteGiftCertificate(anyLong());
    }
}