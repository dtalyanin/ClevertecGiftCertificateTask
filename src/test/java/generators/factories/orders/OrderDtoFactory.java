package generators.factories.orders;

import generators.builders.OrderDtoBuilder;
import ru.clevertec.ecl.dto.orders.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDtoFactory {

    public static OrderDto getSimpleOrderDto() {
        return OrderDtoBuilder.builder().build();
    }

    public static OrderDto getSimpleOrderDto2() {
        return OrderDtoBuilder.builder()
                .quantity(10)
                .totalPrice(BigDecimal.valueOf(10000L, 2))
                .build();
    }

    public static List<OrderDto> getDifferentOrderDtos() {
        return List.of(getSimpleOrderDto(), getSimpleOrderDto2());
    }
}
