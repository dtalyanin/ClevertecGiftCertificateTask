package ru.clevertec.ecl.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.dto.orders.CreateOrderDto;
import ru.clevertec.ecl.dto.orders.OrderDto;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.impl.OrdersServiceImpl;

import java.net.URI;
import java.util.List;

@RestController
@Validated
public class OrdersController {

    private final OrdersServiceImpl service;

    @Autowired
    public OrdersController(OrdersServiceImpl service) {
        this.service = service;
    }

    @GetMapping("users/{userId}/orders")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserIdWithPagination(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(service.getAllOrdersByUserId(userId, pageable));
    }

    @GetMapping("users/{userId}/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrderByOrderIdAndUserId(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long userId,
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long orderId) {
        return ResponseEntity.ok(service.getOrderByOrderIdAndUserId(userId, orderId));
    }

    @PostMapping("users/{userId}/orders")
    public ResponseEntity<ModificationResponse> addOrder(@RequestBody @Valid CreateOrderDto dto,
                                                         @PathVariable @Min(value = 1, message = "Min ID value is 1")
                                                         long userId) {
        ModificationResponse response = service.addOrder(dto, userId);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
