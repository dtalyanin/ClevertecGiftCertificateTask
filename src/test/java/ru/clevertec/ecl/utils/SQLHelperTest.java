package ru.clevertec.ecl.utils;

import org.junit.jupiter.api.Test;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.SQLFilter;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import static generators.factories.FilterCriteriaFactory.*;
import static generators.factories.SQLFilterFactory.*;
import static generators.factories.SortCriteriaFactory.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.clevertec.ecl.utils.SQLHelper.getSQLFilter;
import static ru.clevertec.ecl.utils.SQLHelper.getSQLOrder;

class SQLHelperTest {

    @Test
    void checkGetSQLFilterShouldReturnWithTag() {
        FilterCriteria filter = getFilterByTag();
        SQLFilter expected = getSQLFilterByTag();
        SQLFilter actual = getSQLFilter(filter);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLFilterShouldReturnWithName() {
        FilterCriteria filter = getFilterByName();
        SQLFilter expected = getSQLFilterByName();
        SQLFilter actual = getSQLFilter(filter);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLFilterShouldReturnWithDescription() {
        FilterCriteria filter = getFilterByDescription();
        SQLFilter expected = getSQLFilterByDescription();
        SQLFilter actual = getSQLFilter(filter);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLFilterShouldReturnWithTagNameDescription() {
        FilterCriteria filter = getFilterByAll();
        SQLFilter expected = getSQLFilterByTagNameDescription();
        SQLFilter actual = getSQLFilter(filter);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLFilterShouldReturnFilterWithoutFields() {
        FilterCriteria filter = getEmptyFilter();
        SQLFilter expected = getSQLFilterWithoutFields();
        SQLFilter actual = getSQLFilter(filter);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByName() {
        SortCriteria sorting = getSortByName();
        String expected = " ORDER BY gc.name";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByNameDESC() {
        SortCriteria sorting = getSortByNameDESC();
        String expected = " ORDER BY gc.name DESC";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByDate() {
        SortCriteria sorting = getSortByDate();
        String expected = " ORDER BY gc.createDate";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByDateDESC() {
        SortCriteria sorting = getSortByDateDESC();
        String expected = " ORDER BY gc.createDate DESC";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByDateDESCNameDESC() {
        SortCriteria sorting = getSortByNameDate();
        String expected = " ORDER BY gc.name, gc.createDate";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByNameWithInvalidDateParam() {
        SortCriteria sorting = getSortByNameWithInvalidDateParam();
        String expected = " ORDER BY gc.name";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnOrderByNameDateWithInvalidDateDESCParam() {
        SortCriteria sorting = getSortByNameDateWithInvalidDESCParam();
        String expected = " ORDER BY gc.name, gc.createDate";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldReturnEmptySort() {
        SortCriteria sorting = getEmptySort();
        String expected = "";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGetSQLOrderShouldIgnoreUnknownParamReturnEmptySort() {
        SortCriteria sorting = getEmptySortIgnoringUnknownParam();
        String expected = "";
        String actual = getSQLOrder(sorting);

        assertThat(actual).isEqualTo(expected);
    }
}