package generators.factories;

import ru.clevertec.ecl.models.criteries.FilterCriteria;

public class FilterCriteriaFactory {
    public static FilterCriteria getFilterByTag() {
        return FilterCriteria.builder().tag("Test tag").build();
    }

    public static FilterCriteria getFilterByName() {
        return FilterCriteria.builder().name("t 2").build();
    }

    public static FilterCriteria getFilterByDescription() {
        return FilterCriteria.builder().description("ion 2").build();
    }

    public static FilterCriteria getFilterByAll() {
        return FilterCriteria.builder()
                .tag("Test tag")
                .name("test")
                .description("ion 2")
                .build();
    }
}
