package ru.clevertec.ecl.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.services.UsersService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UsersController {

    private final UsersService service;

    @Autowired
    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsersWithPagination(Pageable pageable) {
        return ResponseEntity.ok(service.getAllUsersWithPagination(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Min(value = 1, message = "Min ID value is 1") Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }
}
