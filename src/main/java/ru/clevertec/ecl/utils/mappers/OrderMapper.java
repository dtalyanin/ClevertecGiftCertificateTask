package ru.clevertec.ecl.utils.mappers;

import org.mapstruct.*;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.models.Order;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = GiftCertificateMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "price", target = "price", qualifiedByName = "fromCoinsToRubles")
    @Mapping(source = "totalPrice", target = "totalPrice", qualifiedByName = "fromCoinsToRubles")
    OrderDto convertOrderToDto(Order order);

    List<OrderDto> convertOrdersToDtos(List<Order> orders);
}
