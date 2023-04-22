package ru.clevertec.ecl.utils.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.models.User;

import java.util.List;

import static generators.factories.users.UserDtoFactory.*;
import static generators.factories.users.UserFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void checkUserToDtoShouldReturnCorrectDto() {
        User user = getSimpleUser();
        UserDto actual = mapper.convertUserToDto(user);
        UserDto expected = getSimpleUserDto();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkUserToDtoShouldReturnNull() {
        UserDto actual = mapper.convertUserToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkUsersToDtosShouldReturnCorrectList() {
        List<User> users = getSimpleUsers();
        List<UserDto> actual = mapper.convertUsersToDtos(users);
        List<UserDto> expected = getSimpleUsersDtos();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkUsersToDtoShouldReturnNull() {
        List<UserDto> actual = mapper.convertUsersToDtos(null);
        assertThat(actual).isNull();
    }
}