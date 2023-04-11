package ru.clevertec.ecl.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.dto.CreateOrderDto;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.services.impl.UsersService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService service;

    @Autowired
    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Min(value = 1, message = "Min ID value is 1") long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable @Min(value = 1, message = "Min ID value is 1") long id) {
        return ResponseEntity.ok(service.getUserOrders(id));
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<String> addOrder(@RequestBody CreateOrderDto dto, @PathVariable Long id) {
        long gen = service.addOrder(id,dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(gen).toUri();
        return ResponseEntity.ok("yes");
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<List<OrderDto>> getUserOrderById(@PathVariable @Min(value = 1, message = "Min ID value is 1") String orderId,
                                                           @PathVariable @Min(value = 1, message = "Min ID value is 1") String userId) {
        return ResponseEntity.ok(null);
    }
}
