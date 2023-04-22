package ru.clevertec.ecl.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.models.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "certificate")
    List<Order> findAllByUserId(Long id, Pageable pageable);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "certificate")
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
