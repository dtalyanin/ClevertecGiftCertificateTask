package ru.clevertec.ecl.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PageableHelper {

    public Pageable setPageableUnsorted(Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());
        }
        return pageable;
    }

    public Pageable validateOnlyNameOrCreateDateOrders(Pageable pageable) {
        List<Sort.Order> orders = pageable.getSort().get()
                .filter(order -> "name".equals(order.getProperty()) || "createdDate".equals(order.getProperty()))
                .collect(Collectors.toList());
        if (orders.size() != pageable.getSort().stream().count()) {
            Sort sort = Sort.by(orders);
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        } else {
            return pageable;
        }
    }
}
