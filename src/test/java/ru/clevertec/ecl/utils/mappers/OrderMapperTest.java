package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.models.Order;

import java.util.List;

import static generators.factories.certificates.GiftCertificateFactory.getSimpleGiftCertificate;
import static generators.factories.orders.CreateOrderDtoFactory.*;
import static generators.factories.orders.OrderDtoFactory.*;
import static generators.factories.orders.OrderFactory.*;
import static generators.factories.users.UserFactory.getSimpleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper mapper;

    @Test
    void checkConvertOrderToDtoShouldReturnCorrectDto() {
        Order order = getSimpleOrder();
        OrderDto actual = mapper.convertOrderToDto(order);
        OrderDto expected = getSimpleOrderDto();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkOrderToDtoShouldReturnNull() {
        OrderDto actual = mapper.convertOrderToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkCreateOrderShouldReturnCorrectOrder() {
        Order actual = mapper.createOrder(getSimpleCreateOrderDto(), getSimpleGiftCertificate(), getSimpleUser());
        Order expected = getSimpleOrderBeforeSaving();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkCreateOrderShouldThrowExceptionCertificateNull() {
        assertThatThrownBy(() -> mapper.createOrder(getSimpleCreateOrderDto(), null, getSimpleUser()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkCreateOrderShouldThrowExceptionUserNull() {
        assertThatThrownBy(() -> mapper.createOrder(getSimpleCreateOrderDto(), getSimpleGiftCertificate(), null))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkCreateOrderShouldThrowExceptionInvalidQuantity() {
        assertThatThrownBy(() -> mapper.createOrder(getCreateOrderDtoWithInvalidQuantity(),
                getSimpleGiftCertificate(), getSimpleUser()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkConvertOrdersToDtosShouldReturnCorrectList() {
        List<Order> orders = getDifferentOrders();
        List<OrderDto> actual = mapper.convertOrdersToDtos(orders);
        List<OrderDto> expected = getDifferentOrderDtos();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkOrdersToDtosShouldReturnNull() {
        List<OrderDto> actual = mapper.convertOrdersToDtos(null);
        assertThat(actual).isNull();
    }
}