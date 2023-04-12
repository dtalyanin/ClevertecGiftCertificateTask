package generators.factories.users;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.dto.UserDto;

import java.util.List;

@UtilityClass
public class UserDtoFactory {
    public UserDto getSimpleUserDto() {
        return new UserDto("Ivan Ivanov", "ivan@ivanov.com");
    }

    public UserDto getSimpleUserDto2() {
        return new UserDto("Petr Petrov", "petr@petrov.com");
    }

    public List<UserDto> getSimpleUsersDtos() {
        return List.of(getSimpleUserDto(), getSimpleUserDto2());
    }
}
