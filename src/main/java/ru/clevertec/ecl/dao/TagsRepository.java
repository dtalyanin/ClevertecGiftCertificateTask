package ru.clevertec.ecl.dao;

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

    @Query(value = "select t.id, t.name, count(*) " +
            "from orders " +
            "inner join gift_certificates gc on orders.certificate_id = gc.id " +
            "inner join gift_certificates_tags gct on gc.id = gct.certificate_id " +
            "inner join tags t on t.id = gct.tag_id " +
            "where user_id = (select user_id from orders group by user_id order by sum(total_price) desc limit 1) " +
            "group by t.id " +
            "order by count(t.id) desc limit 1", nativeQuery = true)
    Tag findMostWidelyUsedTagOfUserWithHighestOrdersCost();

}
