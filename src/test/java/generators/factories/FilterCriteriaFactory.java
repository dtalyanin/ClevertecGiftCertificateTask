package generators.factories;

import ru.clevertec.ecl.models.criteries.FilterCriteria;

import java.util.List;

public class FilterCriteriaFactory {

    public static FilterCriteria getFilterByTag() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag"))
                .build();
    }

    public static FilterCriteria getFilterByTwoTags() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag", "Test tag 3"))
                .build();
    }

    public static FilterCriteria getFilterByUnknownTag() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag unknown"))
                .build();
    }

    public static FilterCriteria getFilterByName() {
        return FilterCriteria.builder()
                .name("t 2")
                .build();
    }

    public static FilterCriteria getFilterByFullName() {
        return FilterCriteria.builder()
                .name("Test 2")
                .build();
    }

    public static FilterCriteria getFilterByPartOfName() {
        return FilterCriteria.builder()
                .name("Test")
                .build();
    }

    public static FilterCriteria getFilterByPartOfNameIgnoreCase() {
        return FilterCriteria.builder()
                .name("tEsT")
                .build();
    }

    public static FilterCriteria getFilterByDescription() {
        return FilterCriteria.builder()
                .description("ion 2")
                .build();
    }

    public static FilterCriteria getFilterByFullDescription() {
        return FilterCriteria.builder()
                .description("Test description 2")
                .build();
    }

    public static FilterCriteria getFilterByPartOfDescription() {
        return FilterCriteria.builder()
                .description("Test")
                .build();
    }

    public static FilterCriteria getFilterByPartOfDescriptionIgnoreCase() {
        return FilterCriteria.builder()
                .description("tEsT")
                .build();
    }

    public static FilterCriteria getFilterByNameAndDescription() {
        return FilterCriteria.builder()
                .name("t 2")
                .description("ion 2")
                .build();
    }

    public static FilterCriteria getFilterByAllFields() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag"))
                .name("test")
                .description("ion 2")
                .build();
    }

    public static FilterCriteria getEmptyFilter() {
        return FilterCriteria.builder().build();
    }
}
