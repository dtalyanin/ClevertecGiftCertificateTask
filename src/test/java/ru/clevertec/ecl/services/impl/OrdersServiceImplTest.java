package ru.clevertec.ecl.services.impl;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.OrdersRepository;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.services.UsersService;

import java.util.List;
import java.util.Optional;

import static generators.factories.PageableFactory.*;
import static generators.factories.certificates.GiftCertificateFactory.*;
import static generators.factories.orders.CreateOrderDtoFactory.*;
import static generators.factories.orders.OrderDtoFactory.*;
import static generators.factories.orders.OrderFactory.*;
import static generators.factories.users.UserFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrdersServiceImplTest {

    @MockBean
    private OrdersRepository repository;
    @MockBean
    private UsersService usersService;
    @MockBean
    private GiftCertificatesService certificatesService;
    @Autowired
    private OrdersServiceImpl service;

    @Test
    void checkGetAllOrdersByUserIdShouldReturnListOfDtos() {
        when(usersService.existsUserById(1L)).thenReturn(true);
        when(repository.findAllByUserId(anyLong(), any(Pageable.class))).thenReturn(getDifferentOrders());

        List<OrderDto> actual = service.getAllOrdersByUserId(1L, getDefaultPageable());
        List<OrderDto> expected = getDifferentOrderDtos();

        assertThat(actual).isEqualTo(expected);
        verify(usersService, times(1)).existsUserById(anyLong());
        verify(repository, times(1)).findAllByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    void checkGetAllOrdersByUserIdShouldThrowExceptionIdNotFound() {
        when(usersService.existsUserById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.getAllOrdersByUserId(1L, getDefaultPageable()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("User with ID 1 not found in database");
        verify(usersService, times(1)).existsUserById(anyLong());
        verify(repository, times(0)).findAllByUserId(anyLong(), any(Pageable.class));
    }

    @Test
    void checkGetOrderByOrderIdAndUserIdShouldReturnOrder() {
        when(usersService.existsUserById(1L)).thenReturn(true);
        when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(getSimpleOrder()));

        OrderDto actual = service.getOrderByOrderIdAndUserId(1L, 1L);
        OrderDto expected = getSimpleOrderDto();

        assertThat(actual).isEqualTo(expected);
        verify(usersService, times(1)).existsUserById(anyLong());
        verify(repository, times(1)).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void checkGetOrderByOrderIdAndUserIdShouldThrowExceptionUserIdNotFound() {
        when(usersService.existsUserById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.getAllOrdersByUserId(1L, getDefaultPageable()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("User with ID 1 not found in database");
        verify(usersService, times(1)).existsUserById(anyLong());
        verify(repository, times(0)).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void checkGetOrderByOrderIdAndUserIdShouldThrowExceptionOrderIdNotFound() {
        when(usersService.existsUserById(1L)).thenReturn(true);
        when(repository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOrderByOrderIdAndUserId(1L, 1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Order with ID 1 not found in database for user with ID 1");
        verify(usersService, times(1)).existsUserById(anyLong());
        verify(repository, times(1)).findByIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void checkAddOrderShouldReturnGeneratedId() {
        when(usersService.getExistingUserById(1L)).thenReturn(Optional.of(getSimpleUser()));
        when(certificatesService.getGiftCertificateByIdWithoutTags(1L))
                .thenReturn(Optional.of(getSimpleGiftCertificate()));
        when(repository.save(any(Order.class))).thenReturn(getSimpleOrder());

        ModificationResponse actual = service.addOrder(getSimpleCreateOrderDto(), 1L);
        ModificationResponse expected = new ModificationResponse(1L, "Order added successfully");

        assertThat(actual).isEqualTo(expected);
        verify(usersService, times(1)).getExistingUserById(anyLong());
        verify(certificatesService, times(1)).getGiftCertificateByIdWithoutTags(anyLong());
        verify(repository, times(1)).save(any(Order.class));
    }


    @Test
    void checkAddOrderShouldThrowExceptionUserIdNotFound() {
        when(usersService.getExistingUserById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addOrder(getSimpleCreateOrderDto(), 1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot add order: user with ID 1 not found in database");
        verify(usersService, times(1)).getExistingUserById(anyLong());
        verify(certificatesService, times(0)).getGiftCertificateByIdWithoutTags(anyLong());
        verify(repository, times(0)).save(any(Order.class));
    }

    @Test
    void checkAddOrderShouldThrowExceptionCertificateIdNotFound() {
        when(usersService.getExistingUserById(1L)).thenReturn(Optional.of(getSimpleUser()));
        when(certificatesService.getGiftCertificateByIdWithoutTags(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addOrder(getSimpleCreateOrderDto(), 1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot add order: gift certificate with ID 1 not found in database");
        verify(usersService, times(1)).getExistingUserById(anyLong());
        verify(certificatesService, times(1)).getGiftCertificateByIdWithoutTags(anyLong());
        verify(repository, times(0)).save(any(Order.class));
    }

    @Test
    void checkAddOrderShouldThrowExceptionInvalidDto() {
        when(usersService.getExistingUserById(1L)).thenReturn(Optional.of(getSimpleUser()));
        when(certificatesService.getGiftCertificateByIdWithoutTags(1L))
                .thenReturn(Optional.of(getSimpleGiftCertificate()));

        assertThatThrownBy(() -> service.addOrder(getCreateOrderDtoWithInvalidQuantity(), 1L))
                .isInstanceOf(ConstraintViolationException.class);
        verify(usersService, times(1)).getExistingUserById(anyLong());
        verify(certificatesService, times(1)).getGiftCertificateByIdWithoutTags(anyLong());
        verify(repository, times(0)).save(any(Order.class));
    }
}