package ru.clevertec.ecl.dao.impl;

import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.models.GiftCertificate;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Repository
public interface GiftCertificatesRepository extends JpaRepository<GiftCertificate, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "tags")
    List<GiftCertificate> findAll();

    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "tags")
    Optional<GiftCertificate> findById(Long aLong);

    boolean existsByNameAndDescriptionAndDurationAndPrice(String name, String description, Duration duration, long price);
    @Modifying
    @Query("delete from GiftCertificate where id = :id")
    int deleteById(long id);
}
