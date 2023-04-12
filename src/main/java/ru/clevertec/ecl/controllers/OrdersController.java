package ru.clevertec.ecl.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.dto.CreateOrderDto;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.services.impl.OrdersService;

import java.net.URI;
import java.util.List;

@RestController
@Validated
public class OrdersController {

    private final OrdersService service;

    @Autowired
    public OrdersController(OrdersService service) {
        this.service = service;
    }

    @GetMapping("users/{userId}/orders")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserIdWithPagination(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(service.getAllOrdersByUserIdWithPagination(userId, pageable));
    }

    @GetMapping("users/{userId}/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrderByOrderIdAndUserId(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") Long userId,
            @PathVariable @Min(value = 1, message = "Min ID value is 1") Long orderId) {
        return ResponseEntity.ok(service.getOrderByOrderIdAndUserId(userId, orderId));
    }

    @PostMapping("users/{userId}/orders")
    public ResponseEntity<String> addOrder(@RequestBody @Valid CreateOrderDto dto,
                                           @PathVariable @Min(value = 1, message = "Min ID value is 1")Long userId) {
        long gen = service.addOrder(dto, userId);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gen).toUri();
        return ResponseEntity.ok("yes");
    }
}
