package ru.clevertec.ecl.integration.services;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.GiftCertificatesRepository;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;
import ru.clevertec.ecl.exceptions.EmptyItemException;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.impl.GiftCertificatesServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static generators.factories.FilterCriteriaFactory.*;
import static generators.factories.PageableFactory.*;
import static generators.factories.certificates.GiftCertificateDtoFactory.*;
import static generators.factories.certificates.GiftCertificateFactory.getSimpleGiftCertificate;
import static generators.factories.certificates.UpdateGiftCertificateDtoFactory.*;
import static generators.factories.responses.ModificationResponseFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GiftCertificatesServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private GiftCertificatesServiceImpl service;
    @Autowired
    private GiftCertificatesRepository repository;

    @Test
    void getAllGiftCertificatesWithFilteringShouldReturn3CertificateDtosWithDefaultPagination() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn2DtosWithPageSize2() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getPageableWithSize2());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbWithSize2();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtosWithFirstPage() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getPageableWithFirstPage());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosEmptyListOutOfPageRange() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getPageableWithOutOfPageRange());

        assertThat(actualCertificateDtos).isEmpty();
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn2DtosWithPageSize2AngIncludeFirstPage() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getPageableWithFirstPageAndSize2());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbWithSize2();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByName() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getSortedByNamePageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos).isEqualTo(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByNameDesc() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getSortedByNameDescPageable());
        System.out.println(actualCertificateDtos);
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbSortByNameDesc();

        assertThat(actualCertificateDtos).isEqualTo(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByCreateDate() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getSortedByCreateDatePageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbSortByNameOrCrateDateAsc();

        assertThat(actualCertificateDtos).isEqualTo(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByCreateDateDesc() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getSortedByCreateDateDescPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbSortByCreateDateDesc();

        assertThat(actualCertificateDtos).isEqualTo(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByCreateDateDescAndNameDesc() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getSortedByCreatedDateDescAndNameDescPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbSortByCreateDateDescAndNameDesc();

        assertThat(actualCertificateDtos).isEqualTo(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldIgnoreUnknownOrders() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(null, getSortedByNameAndCreatedDateAndUnknownPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbSortByNameOrCrateDateAsc();

        assertThat(actualCertificateDtos).isEqualTo(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn1DtoWithFilterByFullName() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByFullName(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getOneGiftCertificateDtosAfterFiltering();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfName() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByPartOfName(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfNameIgnoreCase() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByPartOfNameIgnoreCase(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn1DtoWithFilterByFullDescription() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByFullDescription(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getOneGiftCertificateDtosAfterFiltering();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfDescription() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByPartOfDescription(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfDescriptionIgnoreCase() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByPartOfDescriptionIgnoreCase(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDb();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn2DtoWithFilterByOneTag() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByTag(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getGiftCertificateDtosFromDbWithSize2();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturn1DtoWithFilterByTwoTags() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByTwoTags(), getDefaultPageable());
        List<GiftCertificateDto> expectedCertificateDtos = getOneGiftCertificateDtosAfterFilteringByTags();

        assertThat(actualCertificateDtos)
                .containsAll(expectedCertificateDtos)
                .hasSameSizeAs(expectedCertificateDtos);
    }

    @Test
    void checkGetGiftCertificatesWithFilteringShouldReturnEmptyListUnknownTag() {
        List<GiftCertificateDto> actualCertificateDtos =
                service.getAllGiftCertificatesWithFiltering(getFilterByUnknownTag(), getDefaultPageable());

        assertThat(actualCertificateDtos).isEmpty();
    }

    @Test
    void checkGetGiftCertificateByIdShouldReturnCertificateDtoWithSpecifiedId() {
        GiftCertificateDto actualCertificateDto = service.getGiftCertificateById(1L);
        GiftCertificateDto expectedCertificateDto = getSimpleGiftCertificateDtoWithTags();

        assertThat(actualCertificateDto).isEqualTo(expectedCertificateDto);
    }

    @Test
    void checkGetGiftCertificateByIdShouldThrowExceptionNotFoundId() {
        assertThatThrownBy(() -> service.getGiftCertificateById(10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Gift certificate with ID 10 not found in database");
    }

    @Test
    void checkGetGiftCertificateByIdByIdWithoutTagsShouldReturnCertificate() {
        Optional<GiftCertificate> actualCertificate = service.getGiftCertificateByIdWithoutTags(1L);
        Optional<GiftCertificate> expectedCertificate = Optional.of(getSimpleGiftCertificate());
        assertThat(actualCertificate).isEqualTo(expectedCertificate);
    }

    @Test
    void checkGetGiftCertificateByIdByIdWithoutTagsShouldReturnEmpty() {
        Optional<GiftCertificate> actualCertificate = service.getGiftCertificateByIdWithoutTags(10L);
        assertThat(actualCertificate).isEmpty();
    }

    @Test
    void checkAddGiftCertificateShouldReturnResponseWithGeneratedId() {
        ModificationResponse actualModificationResponse = service.addGiftCertificate(getSimpleGiftCertificateDtoToCreate());
        ModificationResponse expectedModificationResponse = getCertificateAddedResponse();

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
    }

    @Test
    void checkAddGiftCertificateShouldExistInDbAfterExecuting() {
        service.addGiftCertificate(getSimpleGiftCertificateDtoToCreate());
        Optional<GiftCertificate> actualCertificate = repository.findById(4L);

        assertThat(actualCertificate).isPresent();
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionEmptyCertificateName() {
        assertThatThrownBy(() -> service.addGiftCertificate(getGiftCertificateDtoWithEmptyName()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionEmptyCertificateDescription() {
        assertThatThrownBy(() -> service.addGiftCertificate(getGiftCertificateDtoWithEmptyDescription()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionZeroPrice() {
        assertThatThrownBy(() -> service.addGiftCertificate(getGiftCertificateDtoWithZeroPrice()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionZeroDuration() {
        assertThatThrownBy(() -> service.addGiftCertificate(getGiftCertificateDtoWithZeroDuration()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionNullNameDescriptionPriceDuration() {
        assertThatThrownBy(() -> service.addGiftCertificate(getGiftCertificateDtoWithoutFields()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionCertificateExist() {
        assertThatThrownBy(() -> service.addGiftCertificate(getSimpleGiftCertificateDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: gift certificate with similar name, description, price and duration " +
                        "already exist in database");
    }

    @Test
    void checkUpdateGiftCertificateShouldReturnResponseWithUpdatedId() {
        ModificationResponse actualModificationResponse =
                service.updateGiftCertificate(1L, getSimpleUpdateGiftCertificateDto());
        ModificationResponse expectedModificationResponse = getCertificateUpdatedResponse();

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
    }

    @Test
    void checkUpdateGiftCertificateShouldLastUpdateDateBeDifferentAfterExecuting() {
        LocalDateTime certificateLastUpdateBeforeUpdating = repository.findById(1L).get().getLastUpdateDate();
        service.updateGiftCertificate(1L, getSimpleUpdateGiftCertificateDto());
        repository.flush();
        LocalDateTime certificateLastUpdateAfterUpdating = repository.findById(1L).get().getLastUpdateDate();
        assertThat(certificateLastUpdateAfterUpdating).isAfter(certificateLastUpdateBeforeUpdating);
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.updateGiftCertificate(10L, getSimpleUpdateGiftCertificateDto()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot update: gift certificate with ID 10 not found in database");
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionNoFieldsToUpdate() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getUpdateGiftCertificateDtoWithoutFields()))
                .isInstanceOf(EmptyItemException.class)
                .hasMessage("Cannot update: no fields to update");
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionEmptyCertificateName() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getUpdateGiftCertificateDtoWithEmptyName()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionEmptyCertificateDescription() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getUpdateGiftCertificateDtoWithEmptyDescription()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionZeroPrice() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getUpdateGiftCertificateDtoWithZeroPrice()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionZeroDuration() {
        assertThatThrownBy(() -> service.updateGiftCertificate(1L, getUpdateGiftCertificateDtoWithZeroDuration()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionSimilarParamsExist() {
        assertThatThrownBy(() -> service.updateGiftCertificate(3L, getUpdateGiftCertificateDtoWithExistingInDbFields()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: gift certificate with similar name, description, price and duration " +
                        "already exist in database");
    }

    @Test
    void checkDeleteGiftCertificateShouldReturnResponseWithDeletedId() {
        ModificationResponse actualModificationResponse = service.deleteGiftCertificateById(3L);
        ModificationResponse expectedModificationResponse = getCertificateDeletedResponse();

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
    }

    @Test
    void checkDeleteTagShouldNotExistInDbAfterExecuting() {
        service.deleteGiftCertificateById(3L);
        boolean existAfterExecuting = repository.existsById(3L);

        assertThat(existAfterExecuting).isFalse();
    }

    @Test
    void checkDeleteGiftCertificateShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.deleteGiftCertificateById(10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: gift certificate with ID 10 not found in database");
    }
}
