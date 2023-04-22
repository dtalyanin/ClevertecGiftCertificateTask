package generators.factories.orders;

import ru.clevertec.ecl.dto.orders.CreateOrderDto;

public class CreateOrderDtoFactory {

    public static CreateOrderDto getSimpleCreateOrderDto() {
        return new CreateOrderDto(1, 5);
    }

    public static CreateOrderDto getCreateOrderDtoWithInvalidQuantity() {
        return new CreateOrderDto(1, 0);
    }
}
