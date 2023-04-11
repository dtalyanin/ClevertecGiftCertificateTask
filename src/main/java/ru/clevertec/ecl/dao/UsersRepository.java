package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.models.User;

public interface UsersRepository extends JpaRepository<User, Long> {
}
