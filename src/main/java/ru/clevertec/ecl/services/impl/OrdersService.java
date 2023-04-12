package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.OrdersRepository;
import ru.clevertec.ecl.dto.CreateOrderDto;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.User;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.services.UsersService;
import ru.clevertec.ecl.utils.PageableHelper;
import ru.clevertec.ecl.utils.mappers.OrderMapper;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepository repository;
    private final UsersService usersService;
    private final GiftCertificatesService certificatesService;
    private final OrderMapper mapper;

    @Autowired
    public OrdersService(OrdersRepository repository, UsersService usersService, GiftCertificatesService certificatesService, OrderMapper mapper) {
        this.repository = repository;
        this.usersService = usersService;
        this.certificatesService = certificatesService;
        this.mapper = mapper;
    }

    @Transactional
    public List<OrderDto> getAllOrdersByUserIdWithPagination(Long userId, Pageable pageable) {
        if (!usersService.existsUserById(userId)) {
            throw new ItemNotFoundException("User with ID " + userId + " not found in database",
                    ErrorCode.USER_FOR_ORDER_NOT_FOUND);
        }
        pageable = PageableHelper.setPageableUnsorted(pageable);
        List<Order> orders = repository.findAllByUserId(userId, pageable);
        return mapper.convertOrdersToDtos(orders);
    }

    @Transactional
    public OrderDto getOrderByOrderIdAndUserId(Long userId, Long orderId) {
        if (!usersService.existsUserById(userId)) {
            throw new ItemNotFoundException("User with ID " + userId + " not found in database",
                    ErrorCode.USER_FOR_ORDER_NOT_FOUND);
        }
        Optional<Order> order = repository.findByIdAndUserId(orderId, userId);
        if (order.isEmpty()) {
            throw new ItemNotFoundException("Order with ID " + orderId + " not found in database for user with ID " + userId,
                    ErrorCode.ORDER_NOT_FOUND);
        }
        return mapper.convertOrderToDto(order.get());
    }

    @Transactional
    public Long addOrder(CreateOrderDto dto, Long userId) {
        Optional<User> user = usersService.getExistingUserById(userId);
        if (user.isEmpty()) {
            throw new ItemNotFoundException("Cannot add order: user with ID " + userId + " not found in database",
                    ErrorCode.USER_FOR_ORDER_NOT_FOUND);
        }
        Optional<GiftCertificate> certificate = certificatesService.getGiftCertificateByIdWithoutTags(dto.getCertificateId());
        if (certificate.isEmpty()) {
            throw new ItemNotFoundException("Cannot add order: gift certificate with ID " + dto.getCertificateId() + " not found in database",
                    ErrorCode.CERTIFICATE_FOR_ORDER_NOT_FOUND);
        }
        Order order = mapper.createOrder(dto, certificate.get(), user.get());
        repository.save(order);
        return order.getId();
    }
}
