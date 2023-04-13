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
                .tags(List.of("Test tag", "Test tag 2"))
                .build();
    }

    public static FilterCriteria getFilterByName() {
        return FilterCriteria.builder()
                .name("t 2")
                .build();
    }

    public static FilterCriteria getFilterByDescription() {
        return FilterCriteria.builder()
                .description("ion 2")
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
