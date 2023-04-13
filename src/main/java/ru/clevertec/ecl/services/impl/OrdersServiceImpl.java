package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.OrdersRepository;
import ru.clevertec.ecl.dto.orders.CreateOrderDto;
import ru.clevertec.ecl.dto.orders.OrderDto;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.User;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.GiftCertificatesService;
import ru.clevertec.ecl.services.OrdersService;
import ru.clevertec.ecl.services.UsersService;
import ru.clevertec.ecl.utils.mappers.OrderMapper;

import java.util.List;
import java.util.Optional;

import static ru.clevertec.ecl.utils.PageableHelper.*;
import static ru.clevertec.ecl.utils.constants.MessageConstants.*;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository repository;
    private final UsersService usersService;
    private final GiftCertificatesService certificatesService;
    private final OrderMapper mapper;

    @Autowired
    public OrdersServiceImpl(OrdersRepository repository,
                             UsersService usersService,
                             GiftCertificatesService certificatesService,
                             OrderMapper mapper) {
        this.repository = repository;
        this.usersService = usersService;
        this.certificatesService = certificatesService;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrdersByUserId(Long userId, Pageable pageable) {
        checkUserExist(userId);
        pageable = setPageableUnsorted(pageable);
        List<Order> orders = repository.findAllByUserId(userId, pageable);
        return mapper.convertOrdersToDtos(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderByOrderIdAndUserId(Long userId, Long orderId) {
        checkUserExist(userId);
        Optional<Order> order = repository.findByIdAndUserId(orderId, userId);
        if (order.isEmpty()) {
            throw new ItemNotFoundException(ORDER_ID_START + orderId + ORDER_USER_NOT_FOUND + userId,
                    ErrorCode.ORDER_NOT_FOUND);
        }
        return mapper.convertOrderToDto(order.get());
    }

    @Override
    @Transactional
    public ModificationResponse addOrder(CreateOrderDto dto, long userId) {
        Optional<User> user = usersService.getExistingUserById(userId);
        if (user.isEmpty()) {
            throw new ItemNotFoundException(CANNOT_ADD_ORDER + USER_ID_START_L + userId + NOT_FOUND,
                    ErrorCode.USER_FOR_ORDER_NOT_FOUND);
        }
        Optional<GiftCertificate> certificate = certificatesService
                .getGiftCertificateByIdWithoutTags(dto.getCertificateId());
        if (certificate.isEmpty()) {
            throw new ItemNotFoundException(CANNOT_ADD_ORDER + CERTIFICATE_ID_START_L + dto.getCertificateId() +
                    NOT_FOUND, ErrorCode.CERTIFICATE_FOR_ORDER_NOT_FOUND);
        }
        Order order = mapper.createOrder(dto, certificate.get(), user.get());
        long generatedId = repository.save(order).getId();
        return new ModificationResponse(generatedId, ORDER_ADDED);
    }

    private void checkUserExist(long userId) {
        if (!usersService.existsUserById(userId)) {
            throw new ItemNotFoundException(USER_ID_START + userId + NOT_FOUND, ErrorCode.USER_FOR_ORDER_NOT_FOUND);
        }
    }
}
