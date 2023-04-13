package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.models.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
}
