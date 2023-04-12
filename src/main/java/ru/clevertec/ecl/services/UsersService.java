package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UserDto> getAllUsersWithPagination(Pageable pageable);
    UserDto getUserById(Long id);
    Optional<User> getExistingUserById(Long id);
    boolean existsUserById(Long id);
}
