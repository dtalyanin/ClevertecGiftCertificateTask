package generators.factories.users;

import ru.clevertec.ecl.dto.UserDto;

import java.util.List;

public class UserDtoFactory {
    public static UserDto getSimpleUserDto() {
        return new UserDto("Ivan Ivanov", "ivan@ivanov.com");
    }

    public static UserDto getSimpleUserDto2() {
        return new UserDto("Petr Petrov", "petr@petrov.com");
    }

    public static List<UserDto> getSimpleUsersDtos() {
        return List.of(getSimpleUserDto(), getSimpleUserDto2());
    }
}
