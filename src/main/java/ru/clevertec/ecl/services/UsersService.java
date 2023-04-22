package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    List<UserDto> getAllUsers(Pageable pageable);
    UserDto getUserById(long id);
    Optional<User> getExistingUserById(long id);
    boolean existsUserById(long id);
}
