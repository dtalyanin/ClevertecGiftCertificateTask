package ru.clevertec.ecl.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.models.GiftCertificate;

import java.util.List;

import static generators.factories.FilterCriteriaFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.utils.SpecificationHelper.*;

@SpringBootTest
class SpecificationHelperTest {

    @Test
    void checkGetSpecificationsFromFilterShouldReturnOneSpecificationByTag() {
        List<Specification<GiftCertificate>> specifications = getSpecificationsFromFilter(getFilterByTag());
        assertThat(specifications).hasSize(1);
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnOneSpecificationByTwoTags() {
        List<Specification<GiftCertificate>> specifications = getSpecificationsFromFilter(getFilterByTwoTags());
        assertThat(specifications).hasSize(2);
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnOneSpecificationByName() {
        List<Specification<GiftCertificate>> specifications = getSpecificationsFromFilter(getFilterByName());
        assertThat(specifications).hasSize(1);
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnOneSpecificationByDescription() {
        List<Specification<GiftCertificate>> specifications = getSpecificationsFromFilter(getFilterByDescription());
        assertThat(specifications).hasSize(1);
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnOneSpecificationByNameAndDescription() {
        List<Specification<GiftCertificate>> specifications =
                getSpecificationsFromFilter(getFilterByNameAndDescription());
        assertThat(specifications).hasSize(2);
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnOneSpecificationByAllFields() {
        List<Specification<GiftCertificate>> specifications =
                getSpecificationsFromFilter(getFilterByAllFields());
        assertThat(specifications).hasSize(3);
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnEmptyList() {
        List<Specification<GiftCertificate>> specifications =
                getSpecificationsFromFilter(getEmptyFilter());
        assertThat(specifications).isEmpty();
    }

    @Test
    void checkGetSpecificationsFromFilterShouldReturnEmptyListBecauseFilterIsNull() {
        List<Specification<GiftCertificate>> specifications =
                getSpecificationsFromFilter(null);
        assertThat(specifications).isEmpty();
    }
}