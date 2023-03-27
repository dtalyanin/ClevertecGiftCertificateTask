package generators.factories;

import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.Collections;
import java.util.List;

public class SortCriteriaFactory {
    public static SortCriteria getSortByName() {
        return new SortCriteria(Collections.singletonList("name"));
    }
    public static SortCriteria getSortByNameDESC() {
        return new SortCriteria(Collections.singletonList("name:desc"));
    }

    public static SortCriteria getSortByDate() {
        return new SortCriteria(Collections.singletonList("date:asc"));
    }
    public static SortCriteria getSortByDateDESC() {
        return new SortCriteria(Collections.singletonList("date:desc"));
    }

    public static SortCriteria getSortByDateDESCNameDESC() {
        return new SortCriteria(List.of("date:desc", "name:desc"));
    }
}
