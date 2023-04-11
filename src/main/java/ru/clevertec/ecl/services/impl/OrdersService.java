package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.OrdersRepository;
import ru.clevertec.ecl.models.Order;

import java.util.List;

@Service
public class OrdersService {
    private final OrdersRepository repository;

    @Autowired
    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Order> getAllOrdersByUserId(Long id) {
        return repository.findByUserId(id);
    }

    @Transactional
    public Long addOrder(Order order) {
        System.out.println(order);
        repository.save(order);
        return order.getId();
    }
}
