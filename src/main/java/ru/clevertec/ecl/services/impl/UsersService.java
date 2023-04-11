package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dao.UsersRepository;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.User;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.utils.mappers.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UsersService(UsersRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserDto> getAllUsers() {
        return mapper.usersToDtos(repository.findAll());
    }

    public UserDto getUserById(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ItemNotFoundException("User with ID " + id + " not found in database",
                    ErrorCode.USER_ID_NOT_FOUND);
        }
        return mapper.userToDto(user.get());
    }
}
