package generators.factories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableFactory {

    public static Pageable getDefaultPageable() {
        return PageRequest.of(0, 20);
    }

    public static Pageable getSortedByNamePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name");
    }

    public static Pageable getSortedByNameAndCreatedDatePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name", "createdDate");
    }

    public static Pageable getSortedByNameAndCreatedDateAndUnknownPageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name", "createdDate", "unknown");
    }
}
