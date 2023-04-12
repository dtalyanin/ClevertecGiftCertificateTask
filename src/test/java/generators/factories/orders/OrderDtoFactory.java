package generators.factories.orders;

import generators.builders.OrderDtoBuilder;
import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class OrderDtoFactory {
    public OrderDto getSimpleOrderDto() {
        return OrderDtoBuilder.builder().build();
    }

    public OrderDto getSimpleOrderDto2() {
        return OrderDtoBuilder.builder()
                .quantity(10)
                .totalPrice(BigDecimal.valueOf(10000L, 2))
                .build();
    }

    public List<OrderDto> getDifferentOrderDtos() {
        return List.of(getSimpleOrderDto(), getSimpleOrderDto2());
    }
}
