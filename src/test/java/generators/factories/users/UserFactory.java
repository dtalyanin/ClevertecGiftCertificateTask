package generators.factories.users;

import ru.clevertec.ecl.models.User;

import java.util.Collections;
import java.util.List;

public class UserFactory {
    public static User getSimpleUser() {
        return new User(1L, "Ivan", "Ivanov", "ivan@ivanov.com", Collections.emptySet());
    }

    public static User getSimpleUser2() {
        return new User(2L, "Petr", "Petrov", "petr@petrov.com", Collections.emptySet());
    }

    public static User getSimpleUser3() {
        return new User(3L, "Alexandr", "Alexandrov", "alexandr@alexandrov.com",
                Collections.emptySet());
    }

    public static List<User> getSimpleUsers() {
        return List.of(getSimpleUser(), getSimpleUser2(), getSimpleUser3());
    }
}
