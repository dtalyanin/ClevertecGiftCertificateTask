package generators.factories;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.models.criteries.FilterCriteria;

import java.util.List;

@UtilityClass
public class FilterCriteriaFactory {

    public FilterCriteria getFilterByTag() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag"))
                .build();
    }

    public FilterCriteria getFilterByTwoTags() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag", "Test tag 2"))
                .build();
    }

    public FilterCriteria getFilterByName() {
        return FilterCriteria.builder()
                .name("t 2")
                .build();
    }

    public FilterCriteria getFilterByDescription() {
        return FilterCriteria.builder()
                .description("ion 2")
                .build();
    }

    public FilterCriteria getFilterByNameAndDescription() {
        return FilterCriteria.builder()
                .name("t 2")
                .description("ion 2")
                .build();
    }

    public FilterCriteria getFilterByAllFields() {
        return FilterCriteria.builder()
                .tags(List.of("Test tag"))
                .name("test")
                .description("ion 2")
                .build();
    }

    public FilterCriteria getEmptyFilter() {
        return FilterCriteria.builder().build();
    }
}
