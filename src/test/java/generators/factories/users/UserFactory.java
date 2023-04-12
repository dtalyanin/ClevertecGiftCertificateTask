package generators.factories.users;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.models.User;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class UserFactory {
    public User getSimpleUser() {
        return new User(1L, "Ivan", "Ivanov", "ivan@ivanov.com", Collections.emptySet());
    }

    public User getSimpleUser2() {
        return new User(2L, "Petr", "Petrov", "petr@petrov.com", Collections.emptySet());
    }

    public List<User> getSimpleUsers() {
        return List.of(getSimpleUser(), getSimpleUser2());
    }
}
