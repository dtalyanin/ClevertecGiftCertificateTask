package ru.clevertec.ecl.dao.impl;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.models.GiftCertificate;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftCertificatesRepository extends JpaRepository<GiftCertificate, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "tags")
    List<GiftCertificate> findAll();

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "tags")
    Optional<GiftCertificate> findById(long id);

    @Override
    @QueryHints(value = @QueryHint(name = HibernateHints.HINT_FLUSH_MODE, value = "COMMIT"))
    <S extends GiftCertificate> boolean exists(Example<S> example);

    @Modifying
    @Query("delete from GiftCertificate where id = :id")
    int deleteById(long id);
}
