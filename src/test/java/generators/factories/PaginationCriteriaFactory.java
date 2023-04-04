package generators.factories;

import ru.clevertec.ecl.models.criteries.PaginationCriteria;

public class PaginationCriteriaFactory {
    public static PaginationCriteria getPaginationFrom0To10() {
        return new PaginationCriteria(0, 10);
    }
}
