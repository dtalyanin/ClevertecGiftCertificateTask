package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.UsersRepository;
import ru.clevertec.ecl.dto.CreateOrderDto;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.User;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.utils.mappers.OrderMapper;
import ru.clevertec.ecl.utils.mappers.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final OrdersService ordersService;
    private final GiftCertificatesService certificatesService;
    private final UserMapper mapper;
    private final OrderMapper orderMapper;

    @Autowired
    public UsersService(UsersRepository repository, OrdersService ordersService, GiftCertificatesService certificatesService, UserMapper mapper, OrderMapper orderMapper) {
        this.repository = repository;
        this.ordersService = ordersService;
        this.certificatesService = certificatesService;
        this.mapper = mapper;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return mapper.usersToDtos(repository.findAll());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new ItemNotFoundException("User with ID " + id + " not found in database",
                    ErrorCode.USER_ID_NOT_FOUND);
        }
        return mapper.userToDto(user.get());
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(Long id) {
        List<Order> orders = ordersService.getAllOrdersByUserId(id);
        if (orders.size() == 0) {
            throw new ItemNotFoundException("User with ID " + id + " not found in database",
                    ErrorCode.USER_ID_NOT_FOUND);
        }
        return orderMapper.convertOrdersToDtos(orders);
    }
    @Transactional
    public Long addOrder(Long userId, CreateOrderDto dto) {
        Optional<User> user = repository.findById(userId);
        //надо новые ошибки добавить
        if (user.isEmpty()) {
            throw new ItemNotFoundException("Cannot add order: user with ID " + userId + " not found in database",
                    ErrorCode.USER_ID_NOT_FOUND);
        }
        Optional<GiftCertificate> certificate = certificatesService.getOptionalGiftCertificateById(dto.getCertificateId());
        if (certificate.isEmpty()) {
            throw new ItemNotFoundException("Gift certificate with ID " + dto.getCertificateId() + " not found in database",
                    ErrorCode.CERTIFICATE_ID_NOT_FOUND);
        }
        Order order = new Order();
        order.setQuantity(dto.getQuantity());
        order.setUser(user.get());
        order.setCertificate(certificate.get());
        return ordersService.addOrder(order);
    }
}
