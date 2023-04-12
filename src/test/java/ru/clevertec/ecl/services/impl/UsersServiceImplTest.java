package ru.clevertec.ecl.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.UsersRepository;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.User;

import java.util.List;
import java.util.Optional;

import static generators.factories.PageFactory.*;
import static generators.factories.PageableFactory.getDefaultPageable;
import static generators.factories.users.UserDtoFactory.*;
import static generators.factories.users.UserFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsersServiceImplTest {

    @MockBean
    private UsersRepository repository;
    @Autowired
    private UsersServiceImpl service;

    @Test
    void checkGetAllUsersShouldReturnListOfDtos() {
        when(repository.findAll(any(Pageable.class))).thenReturn(getUserPage());

        List<UserDto> actual = service.getAllUsers(getDefaultPageable());
        List<UserDto> expected = getSimpleUsersDtos();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void checkGetUserByIdShouldReturnUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(getSimpleUser()));

        UserDto actual = service.getUserById(1);
        UserDto expected = getSimpleUserDto();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkGetUserByIdShouldThrowExceptionIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserById(1))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("User with ID 1 not found in database");
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkGetExistingUserByIdShouldReturnUserOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(getSimpleUser()));

        Optional<User> actual = service.getExistingUserById(1L);
        Optional<User> expected = Optional.of(getSimpleUser());

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkExistsUserByIdShouldReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean actual = service.existsUserById(1L);

        assertThat(actual).isTrue();
        verify(repository, times(1)).existsById(anyLong());
    }
}