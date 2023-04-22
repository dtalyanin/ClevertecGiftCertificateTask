package ru.clevertec.ecl.integration.services;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.OrdersRepository;
import ru.clevertec.ecl.dto.orders.OrderDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.impl.OrdersServiceImpl;

import java.util.List;

import static generators.factories.PageableFactory.*;
import static generators.factories.orders.CreateOrderDtoFactory.*;
import static generators.factories.orders.OrderDtoFactory.*;
import static generators.factories.responses.ModificationResponseFactory.getOrderAddedResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrdersServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrdersServiceImpl service;
    @Autowired
    private OrdersRepository repository;

    @Test
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithDefaultPagination() {
        List<OrderDto> actualOrderDtos = service.getAllOrdersByUserId(1L, getDefaultPageable());
        List<OrderDto> expectedOrderDtos = getDifferentOrderDtos();

        assertThat(actualOrderDtos)
                .containsAll(expectedOrderDtos)
                .hasSameSizeAs(expectedOrderDtos);
    }

    @Test
    void checkGetAllOrdersByUserIdShouldReturn1OrderDtoWithPageSize1() {
        List<OrderDto> actualOrderDtos = service.getAllOrdersByUserId(1L, getPageableWithSize1());
        List<OrderDto> expectedOrderDtos = getOrderDtosWithSize1();

        assertThat(actualOrderDtos).isEqualTo(expectedOrderDtos);
    }

    @Test
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithFirstPage() {
        List<OrderDto> actualOrderDtos = service.getAllOrdersByUserId(1L, getPageableWithFirstPage());
        List<OrderDto> expectedOrderDtos = getDifferentOrderDtos();

        assertThat(actualOrderDtos)
                .containsAll(expectedOrderDtos)
                .hasSameSizeAs(expectedOrderDtos);
    }

    @Test
    void checkGetAllOrdersByUserIdShouldReturnEmptyListOutOfPageRange() {
        List<OrderDto> actualOrderDtos = service.getAllOrdersByUserId(1L, getPageableWithOutOfPageRange());

        assertThat(actualOrderDtos).isEmpty();
    }

    @Test
    void checkGetAllOrdersByUserIdShouldReturn1OrderDtoWithPageSize1AngIncludeFirstPage() {
        List<OrderDto> actualOrderDtos = service.getAllOrdersByUserId(1L, getPageableWithFirstPageAndSize1());
        List<OrderDto> expectedOrderDtos = getOrderDtosWithSize1();

        assertThat(actualOrderDtos).isEqualTo(expectedOrderDtos);
    }

    @Test
    void checkGetAllOrdersByUserIdShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.getAllOrdersByUserId(10L, getDefaultPageable()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("User with ID 10 not found in database");
    }


    @Test
    void checkGetOrderByOrderIdAndUserIdShouldReturnOrder() {
        OrderDto actualOrderDto = service.getOrderByOrderIdAndUserId(1L, 1L);
        OrderDto expectedOrderDto = getSimpleOrderDto();

        assertThat(actualOrderDto).isEqualTo(expectedOrderDto);
    }

    @Test
    void checkGetOrderByOrderIdAndUserIdShouldThrowExceptionUserIdNotFound() {
        assertThatThrownBy(() -> service.getOrderByOrderIdAndUserId(10L, 1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("User with ID 10 not found in database");
    }

    @Test
    void checkGetOrderByOrderIdAndUserIdShouldThrowExceptionOrderIdNotFound() {
        assertThatThrownBy(() -> service.getOrderByOrderIdAndUserId(1L, 10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Order with ID 10 not found in database for user with ID 1");
    }

    @Test
    void checkAddOrderShouldReturnGeneratedId() {
        ModificationResponse actualModificationResponse = service.addOrder(getSimpleCreateOrderDto(), 1L);
        ModificationResponse expectedModificationResponse = getOrderAddedResponse();
        boolean existAfterExecuting = repository.existsById(4L);

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
        assertThat(existAfterExecuting).isTrue();
    }

    @Test
    void checkAddOrderShouldExistInDbAfterExecuting() {
        service.addOrder(getSimpleCreateOrderDto(), 1L);
        boolean existAfterExecuting = repository.existsById(4L);

        assertThat(existAfterExecuting).isTrue();
    }

    @Test
    void checkAddOrderShouldThrowExceptionUserIdNotFound() {
        assertThatThrownBy(() -> service.addOrder(getSimpleCreateOrderDto(), 10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot add order: user with ID 10 not found in database");
    }

    @Test
    void checkAddOrderShouldThrowExceptionCertificateIdNotFound() {
        assertThatThrownBy(() -> service.addOrder(getCreateOrderDtoWithNotExistingCertificateId(), 1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot add order: gift certificate with ID 10 not found in database");
    }

    @Test
    void checkAddOrderShouldThrowExceptionIncorrectQuantity() {
        assertThatThrownBy(() -> service.addOrder(getCreateOrderDtoWithInvalidQuantity(), 1L))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
