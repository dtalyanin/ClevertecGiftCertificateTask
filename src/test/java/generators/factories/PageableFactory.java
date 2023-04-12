package generators.factories;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PageableFactory {

    public Pageable getDefaultPageable() {
        return PageRequest.of(0, 20);
    }
}
