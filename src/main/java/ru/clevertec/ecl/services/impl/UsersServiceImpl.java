package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.UsersRepository;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.User;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.services.UsersService;
import ru.clevertec.ecl.utils.PageableHelper;
import ru.clevertec.ecl.utils.mappers.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UsersServiceImpl(UsersRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(Pageable pageable) {
        pageable = PageableHelper.setPageableUnsorted(pageable);
        List<User> users = repository.findAll(pageable).getContent();
        return mapper.convertUsersToDtos(users);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(long id) {
        Optional<User> user = getExistingUserById(id);
        if (user.isEmpty()) {
            throw new ItemNotFoundException("User with ID " + id + " not found in database",
                    ErrorCode.USER_ID_NOT_FOUND);
        }
        return mapper.convertUserToDto(user.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getExistingUserById(long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUserById(long id) {
        return repository.existsById(id);
    }
}
