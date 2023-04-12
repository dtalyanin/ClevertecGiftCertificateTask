package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.CreateOrderDto;
import ru.clevertec.ecl.dto.OrderDto;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.User;

import java.util.List;

@Validated
@Mapper(componentModel = "spring",
        uses = GiftCertificateMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderDto convertOrderToDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "certificate", source = "certificate")
    @Valid Order createOrder(CreateOrderDto dto, GiftCertificate certificate, User user);

    List<OrderDto> convertOrdersToDtos(List<Order> orders);
}
