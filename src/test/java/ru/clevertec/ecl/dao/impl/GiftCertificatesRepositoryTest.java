package ru.clevertec.ecl.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.configuration.TestConfig;
import ru.clevertec.ecl.dao.GiftCertificatesDAO;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static generators.factories.FilterCriteriaFactory.*;
import static generators.factories.GiftCertificateFactory.*;
import static generators.factories.SortCriteriaFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
@Rollback
class GiftCertificatesRepositoryTest {
    @Autowired
    private GiftCertificatesDAO dao;

    @Test
    void checkGetAllGiftCertificatesWithoutFiltersAndSorting() {
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, null, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2(),
                getSimpleGiftCertificate3());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithFiltersByTag() {
        FilterCriteria filter = getFilterByTag();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(filter, null, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithFiltersByName() {
        FilterCriteria filter = getFilterByName();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(filter, null, null);
        List<GiftCertificate> expected = Collections.singletonList(getSimpleGiftCertificate2());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithFiltersByDescription() {
        FilterCriteria filter = getFilterByDescription();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(filter, null, null);
        List<GiftCertificate> expected = Collections.singletonList(getSimpleGiftCertificate2());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithFiltersByTagNameDescription() {
        FilterCriteria filter = getFilterByAll();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(filter, null, null);
        List<GiftCertificate> expected = Collections.singletonList(getSimpleGiftCertificate2());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithSortingByName() {
        SortCriteria sort = getSortByName();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2(),
                getSimpleGiftCertificate3());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithSortingByNameDESC() {
        SortCriteria sort = getSortByNameDESC();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate3(), getSimpleGiftCertificate2(),
                getSimpleGiftCertificate());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithSortingByDate() {
        SortCriteria sort = getSortByDate();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2(),
                getSimpleGiftCertificate3());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithSortingByDateDESC() {
        SortCriteria sort = getSortByDateDESC();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate3(), getSimpleGiftCertificate(),
                getSimpleGiftCertificate2());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithSortingByDateDESCNameDESC() {
        SortCriteria sort = getSortByDateDESCNameDESC();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate3(), getSimpleGiftCertificate2(),
                getSimpleGiftCertificate());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithoutSortingWrongKey() {
        SortCriteria sort = new SortCriteria(Collections.singletonList("namee:desc"));
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2(),
                getSimpleGiftCertificate3());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithFilterByTagSortingByNameDESC() {
        FilterCriteria filter = getFilterByTag();
        SortCriteria sort = getSortByNameDESC();
        List<GiftCertificate> actual = dao.getAllGiftCertificates(filter, sort, null);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate2(), getSimpleGiftCertificate());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetAllGiftCertificatesWithPaginationBy2Values() {
        PaginationCriteria pagination = new PaginationCriteria(0, 2);
        List<GiftCertificate> actual = dao.getAllGiftCertificates(null, null, pagination);
        List<GiftCertificate> expected = List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetGiftCertificateById() {
        Optional<GiftCertificate> actual = dao.getGiftCertificateById(1L);
        Optional<GiftCertificate> expected = Optional.of(getSimpleGiftCertificate());
        assertThat(actual)
                .isPresent()
                .isEqualTo(expected);
    }

    @Test
    void checkGetGiftCertificateByIdShouldReturnEmptyOptional() {
        Optional<GiftCertificate> actual = dao.getGiftCertificateById(4L);
        assertThat(actual).isEmpty();
    }

    @Test
    void checkAddGiftCertificateShouldReturnGeneratedId() {
        long actual = dao.addGiftCertificate(getGiftCertificateWithAllUpdatedFields()).getId();
        assertThat(actual).isNotZero();
    }

    @Test
    void checkAddGiftCertificateShouldThrowExceptionTagWithNameExist() {
        assertThatThrownBy(() -> dao.addGiftCertificate(getSimpleGiftCertificateWithoutId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void checkUpdateGiftCertificateShouldUpdate() {
        GiftCertificate expectedUpdatedCertificate = getGiftCertificateForUpdate();
        dao.updateGiftCertificate(3L, expectedUpdatedCertificate);
        GiftCertificate actualUpdatedCertificate = dao.getGiftCertificateById(3L).get();
        assertThat(actualUpdatedCertificate).isEqualTo(expectedUpdatedCertificate);
    }

    @Test
    void checkUpdateGiftCertificateShouldThrowExceptionWithSameParamsAlreadyExist() {
        assertThatThrownBy(() -> dao.updateGiftCertificate(2L, getSimpleGiftCertificate()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void checkDeleteGiftCertificateShouldDeleteRow() {
        boolean certificateDeleted = dao.deleteGiftCertificate(1L);
        Optional<GiftCertificate> deletedCertificate = dao.getGiftCertificateById(1L);
        assertThat(certificateDeleted).isTrue();
        assertThat(deletedCertificate).isEmpty();
    }

    @Test
    void checkDeleteGiftCertificateShouldNotDeleteWrongId() {
        boolean certificateDeleted = dao.deleteGiftCertificate(4L);
        assertThat(certificateDeleted).isFalse();
    }
}