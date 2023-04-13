package ru.clevertec.ecl.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static generators.factories.PageableFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.ecl.utils.PageableHelper.*;

@SpringBootTest
class PageableHelperTest {

    @Test
    void checkSetPageableUnsortedShouldNotChangeWithAlreadyUnsorted() {
        Pageable actual = setPageableUnsorted(getDefaultPageable());
        Pageable expected = getDefaultPageable();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkSetPageableUnsortedShouldChangeToUnsorted() {
        Pageable actual = setPageableUnsorted(getSortedByNamePageable());
        Pageable expected = getDefaultPageable();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkValidateOnlyNameOrCreateDateOrdersShouldNotChangeNameOrder() {
        Pageable actual = validateOnlyNameOrCreateDateOrders(getSortedByNamePageable());
        Pageable expected = getSortedByNamePageable();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkValidateOnlyNameOrCreateDateOrdersShouldNotChangeNameAndCreatedDateOrder() {
        Pageable actual = validateOnlyNameOrCreateDateOrders(getSortedByNameAndCreatedDatePageable());
        Pageable expected = getSortedByNameAndCreatedDatePageable();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkValidateOnlyNameOrCreateDateOrdersShouldDeleteUnknownOrder() {
        Pageable actual = validateOnlyNameOrCreateDateOrders(getSortedByNameAndCreatedDateAndUnknownPageable());
        Pageable expected = getSortedByNameAndCreatedDatePageable();

        assertThat(actual).isEqualTo(expected);
    }
}