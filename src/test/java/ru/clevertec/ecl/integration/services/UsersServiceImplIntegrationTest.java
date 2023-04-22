package ru.clevertec.ecl.integration.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.User;
import ru.clevertec.ecl.services.impl.UsersServiceImpl;

import java.util.List;
import java.util.Optional;

import static generators.factories.PageableFactory.*;
import static generators.factories.users.UserDtoFactory.*;
import static generators.factories.users.UserFactory.getSimpleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UsersServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UsersServiceImpl service;

    @Test
    void checkGetAllUsersShouldReturnUserDtosWithDefaultPagination() {
        List<UserDto> expectedUserDtos = getSimpleUsersDtos();
        List<UserDto> actualUserDtos = service.getAllUsers(getDefaultPageable());

        assertThat(actualUserDtos).isEqualTo(expectedUserDtos);
    }

    @Test
    void checkGetAllUsersShouldReturn1UserDtoWithPageSize1() {
        List<UserDto> expectedUserDtos = getUserDtosWithSize1();
        List<UserDto> actualUserDtos = service.getAllUsers(getPageableWithSize1());

        assertThat(actualUserDtos).isEqualTo(expectedUserDtos);
    }

    @Test
    void checkGetAllUsersShouldReturn3UserDtosWithFirstPage() {
        List<UserDto> expectedUserDtos = getSimpleUsersDtos();
        List<UserDto> actualUserDtos = service.getAllUsers(getPageableWithFirstPage());

        assertThat(actualUserDtos).isEqualTo(expectedUserDtos);
    }

    @Test
    void checkGetAllUsersShouldReturnUserDtosEmptyListOutOfPageRange() {
        List<UserDto> expectedUserDtos = getEmptyListUserDtos();
        List<UserDto> actualUserDtos = service.getAllUsers(getPageableWithOutOfPageRange());

        assertThat(actualUserDtos).isEqualTo(expectedUserDtos);
    }

    @Test
    void checkGetAllUsersShouldReturn1UserDtoWithPageSize1AngIncludeFirstPage() {
        List<UserDto> expectedUserDtos = getUserDtosWithSize1();
        List<UserDto> actualUserDtos = service.getAllUsers(getPageableWithFirstPageAndSize1());

        assertThat(actualUserDtos).isEqualTo(expectedUserDtos);
    }

    @Test
    void checkGetUserByIdShouldReturnUserDto() {
        UserDto expectedUserDto = getSimpleUserDto();
        UserDto actualUserDto = service.getUserById(1L);

        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    void checkGetUserByIdShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.getUserById(10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("User with ID 10 not found in database");
    }

    @Test
    void checkGetExistingUserByIdShouldReturnOptionalWithUser() {
        Optional<User> expectedUser = Optional.of(getSimpleUser());
        Optional<User> actualUser = service.getExistingUserById(1L);

        assertThat(actualUser)
                .isPresent()
                .isEqualTo(expectedUser);
    }

    @Test
    void checkGetExistingUserByIdShouldReturnEmptyOptional() {
        Optional<User> actualUser = service.getExistingUserById(10L);

        assertThat(actualUser)
                .isEmpty();
    }

    @Test
    void checkExistsUserByIdShouldReturnTrue() {
        boolean actual = service.existsUserById(1L);
        assertThat(actual).isTrue();
    }

    @Test
    void checkExistsUserByIdShouldReturnFalse() {
        boolean actual = service.existsUserById(10L);
        assertThat(actual).isFalse();
    }
}
