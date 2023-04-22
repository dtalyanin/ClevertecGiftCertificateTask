package ru.clevertec.ecl.dao;

import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.models.GiftCertificate;

import java.util.Optional;

@Repository
public interface GiftCertificatesRepository extends JpaRepository<GiftCertificate, Long>,
        JpaSpecificationExecutor<GiftCertificate> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "tags")
    Optional<GiftCertificate> findById(long id);

    Optional<GiftCertificate> findWithoutTagsById(long id);

    @Override
    @QueryHints(value = @QueryHint(name = HibernateHints.HINT_FLUSH_MODE, value = "COMMIT"))
    <S extends GiftCertificate> boolean exists(Example<S> example);

    @Modifying
    @Query("delete from GiftCertificate where id = :id")
    int deleteById(long id);
}
