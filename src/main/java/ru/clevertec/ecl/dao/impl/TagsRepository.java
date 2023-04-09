package ru.clevertec.ecl.dao.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.models.Tag;

import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    boolean existsByName(String name);
    @Modifying
    @Query("delete from Tag t where t.id = :id")
    int deleteById(long id);
}
