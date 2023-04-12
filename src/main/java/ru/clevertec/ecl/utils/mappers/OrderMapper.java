package ru.clevertec.ecl.utils.mappers;

import org.mapstruct.*;
import ru.clevertec.ecl.dto.CreateOrderDto;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.User;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = GiftCertificateMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "price", target = "price")
    @Mapping(source = "totalPrice", target = "totalPrice")
    OrderDto convertOrderToDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "certificate", source = "certificate")
    Order createOrder(CreateOrderDto dto, GiftCertificate certificate, User user);
    List<OrderDto> convertOrdersToDtos(List<Order> orders);
}
