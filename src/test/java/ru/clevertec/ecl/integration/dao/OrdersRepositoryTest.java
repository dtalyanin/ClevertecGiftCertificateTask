package ru.clevertec.ecl.integration.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.OrdersRepository;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.Order;

import java.util.List;
import java.util.Optional;

import static generators.factories.PageableFactory.getDefaultPageable;
import static generators.factories.orders.OrderFactory.getDifferentOrders;
import static generators.factories.orders.OrderFactory.getSimpleOrder;
import static org.assertj.core.api.Assertions.assertThat;

class OrdersRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private OrdersRepository repository;

    @Test
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithDefaultPagination() {
        List<Order> actualOrders = repository.findAllByUserId(1L, getDefaultPageable());
        List<Order> expectedOrders = getDifferentOrders();

        assertThat(actualOrders)
                .containsAll(expectedOrders)
                .hasSameSizeAs(expectedOrders);
    }

    @Test
    void checkGetAllOrdersByUserIdShouldReturnEmptyListIdNotFound() {
        List<Order> actualOrders = repository.findAllByUserId(10L, getDefaultPageable());
        assertThat(actualOrders).isEmpty();
    }

    @Test
    void checkFindByIdAndUserIdShouldReturnOrder() {
        Optional<Order> actualOptionalOrder = repository.findByIdAndUserId(1L, 1L);
        Order expectedOrder = getSimpleOrder();
        assertThat(actualOptionalOrder)
                .isPresent()
                .hasValue(expectedOrder);
    }

    @Test
    void checkFindByIdAndUserIdShouldReturnEmptyOptionalOrderIdNotFound() {
        Optional<Order> actualOptionalOrder = repository.findByIdAndUserId(10L, 1L);
        assertThat(actualOptionalOrder).isEmpty();
    }

    @Test
    void checkFindByIdAndUserIdShouldReturnEmptyOptionalUserIdNotFound() {
        Optional<Order> actualOptionalOrder = repository.findByIdAndUserId(1L, 10L);
        assertThat(actualOptionalOrder).isEmpty();
    }
}