package generators.factories;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageableFactory {

    public Pageable getDefaultPageable() {
        return PageRequest.of(0, 20);
    }

    public Pageable getSortedByNamePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name");
    }

    public Pageable getSortedByNameAndCreatedDatePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name", "createdDate");
    }

    public Pageable getSortedByNameAndCreatedDateAndUnknownPageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name", "createdDate", "unknown");
    }
}
