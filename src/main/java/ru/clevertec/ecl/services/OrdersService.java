package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.orders.CreateOrderDto;
import ru.clevertec.ecl.dto.orders.OrderDto;
import ru.clevertec.ecl.models.responses.ModificationResponse;

import java.util.List;

public interface OrdersService {
    List<OrderDto> getAllOrdersByUserId(Long userId, Pageable pageable);
    OrderDto getOrderByOrderIdAndUserId(Long userId, Long orderId);
    ModificationResponse addOrder(CreateOrderDto dto, long userId);
}
