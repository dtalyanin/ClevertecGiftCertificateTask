package generators.factories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableFactory {

    public static Pageable getDefaultPageable() {
        return PageRequest.of(0, 20);
    }

    public static Pageable getPageableWithSize1() {
        return PageRequest.of(0, 1);
    }

    public static Pageable getPageableWithSize2() {
        return PageRequest.of(0, 2);
    }

    public static Pageable getPageableWithFirstPage() {
        return PageRequest.of(0, 20);
    }

    public static Pageable getPageableWithOutOfPageRange() {
        return PageRequest.of(2, 20);
    }

    public static Pageable getPageableWithFirstPageAndSize1() {
        return PageRequest.of(0, 1);
    }

    public static Pageable getPageableWithFirstPageAndSize2() {
        return PageRequest.of(0, 2);
    }

    public static Pageable getSortedByNamePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name");
    }

    public static Pageable getSortedByNameDescPageable() {
        return PageRequest.of(0, 20, Sort.Direction.DESC, "name");
    }

    public static Pageable getSortedByCreateDatePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "createDate");
    }

    public static Pageable getSortedByCreateDateDescPageable() {
        return PageRequest.of(0, 20, Sort.Direction.DESC, "createDate");
    }

    public static Pageable getSortedByNameAndCreatedDatePageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name", "createDate");
    }

    public static Pageable getSortedByCreatedDateDescAndNameDescPageable() {
        return PageRequest.of(0, 20, Sort.Direction.DESC, "createDate", "name");
    }

    public static Pageable getSortedByNameAndCreatedDateAndUnknownPageable() {
        return PageRequest.of(0, 20, Sort.Direction.ASC, "name", "createDate", "unknown");
    }
}
