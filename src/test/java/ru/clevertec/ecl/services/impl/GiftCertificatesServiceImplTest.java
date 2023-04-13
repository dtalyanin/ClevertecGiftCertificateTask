package ru.clevertec.ecl.services.impl;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.dao.GiftCertificatesRepository;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.TagsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static generators.factories.PageFactory.*;
import static generators.factories.PageableFactory.*;
import static generators.factories.certificates.GiftCertificateDtoFactory.*;
import static generators.factories.certificates.GiftCertificateFactory.*;
import static generators.factories.certificates.UpdateGiftCertificateDtoFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@SpringBootTest
class GiftCertificatesServiceImplTest {

    @MockBean
    private GiftCertificatesRepository repository;
    @MockBean
    private TagsService tagsService;
    @Autowired
    private GiftCertificatesServiceImpl service;

    @Test
    void getAllGiftCertificatesWithFilteringShouldReturnListOfDtos() {
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(getCertificatePage());

        List<GiftCertificateDto> actual = service.getAllGiftCertificatesWithFiltering(null, getDefaultPageable());
        List<GiftCertificateDto> expected = getSimpleGiftCertificateDtos();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void checkGetGiftCertificateByIdShouldReturnCertificateDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(getSimpleGiftCertificate()));

        GiftCertificateDto actual = service.getGiftCertificateById(1L);
        GiftCertificateDto expected = getSimpleGiftCertificateDto();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkGetGiftCertificateByIdShouldThrowExceptionNotFoundId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getGiftCertificateById(1))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Gift certificate with ID 1 not found in database");
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkGetGiftCertificateByIdByIdWithoutTagsShouldReturnCertificate() {
        when(repository.findWithoutTagsById(1L)).thenReturn(Optional.of(getSimpleGiftCertificate()));

        Optional<GiftCertificate> actual = service.getGiftCertificateByIdWithoutTags(1L);
        Optional<GiftCertificate>expected = Optional.of(getSimpleGiftCertificate());

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findWithoutTagsById(anyLong());
    }

    @Test
    void checkAddGiftCertificateShouldReturnResponseWithGeneratedId() {
        when(repository.exists(any(Example.class))).thenReturn(false);
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());
        when(repository.save(any(GiftCertificate.class))).thenReturn(getSimpleGiftCertificate());

        ModificationResponse actual = service.addGiftCertificate(getSimpleGiftCertificateDto());
        ModificationResponse expected = new ModificationResponse(1L, "Gift certificate added successfully");

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).exists(any(Example.class));
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
        verify(repository, times(1)).save(any(GiftCertificate.class));
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionInvalidDto() {
        assertThatThrownBy(() -> service.addGiftCertificate(getGiftCertificateDtoWithoutFields()))
                .isInstanceOf(ConstraintViolationException.class);
        verify(repository, times(0)).exists(any(Example.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anySet());
        verify(repository, times(0)).save(any(GiftCertificate.class));
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionCertificateExist() {
        when(repository.exists(any(Example.class))).thenReturn(true);

        assertThatThrownBy(() -> service.addGiftCertificate(getSimpleGiftCertificateDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: gift certificate with similar name, description, price and duration " +
                        "already exist in database");
        verify(repository, times(1)).exists(any(Example.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anySet());
        verify(repository, times(0)).save(any(GiftCertificate.class));
    }

    @Test
    void checkUpdateGiftCertificateShouldReturnResponseWithUpdatedId() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(repository.save(any(GiftCertificate.class))).thenReturn(getSimpleGiftCertificate());
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());
        when(repository.exists(any(Example.class))).thenReturn(false);

        ModificationResponse actual = service.updateGiftCertificate(1L, getSimpleUpdateGiftCertificateDto());
        ModificationResponse expected = new ModificationResponse(1L, "Gift certificate updated successfully");

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).exists(any(Example.class));
        verify(repository, times(1)).save(any(GiftCertificate.class));
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getSimpleUpdateGiftCertificateDto()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot update: gift certificate with ID 1 not found");
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).exists(any(Example.class));
        verify(repository, times(0)).save(any(GiftCertificate.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionNoFieldsToUpdate() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getUpdateGiftCertificateDtoWithoutFields()))
                .isInstanceOf(EmptyItemException.class)
                .hasMessage("Cannot update: no fields to update");
        verify(repository, times(0)).findById(anyLong());
        verify(repository, times(0)).exists(any(Example.class));
        verify(repository, times(0)).save(any(GiftCertificate.class));
        verify(tagsService, times(0)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionCertificateExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(tagsService.addAllTagsIfNotExist(anySet())).thenReturn(Collections.emptySet());
        when(repository.exists(any(Example.class))).thenReturn(true);

        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getSimpleUpdateGiftCertificateDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: gift certificate with similar name, description, price and duration " +
                        "already exist in database");
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).exists(any(Example.class));
        verify(repository, times(0)).save(any(GiftCertificate.class));
        verify(tagsService, times(1)).addAllTagsIfNotExist(anySet());
    }

    @Test
    void checkDeleteGiftCertificateShouldReturnResponseWithDeletedId() {
        when(repository.deleteById(anyLong())).thenReturn(1);

        ModificationResponse actual = service.deleteGiftCertificateById(5L);
        ModificationResponse expected = new ModificationResponse(5L, "Gift certificate deleted successfully");

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void checkDeleteGiftCertificateShouldThrowExceptionNotFound() {
        when(repository.deleteById(anyLong())).thenReturn(0);

        assertThatThrownBy(() -> service.deleteGiftCertificateById(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: gift certificate with ID 1 not found");
        verify(repository, times(1)).deleteById(anyLong());
    }
}