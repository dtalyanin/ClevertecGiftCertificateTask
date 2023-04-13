package generators.factories.orders;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.dto.orders.CreateOrderDto;

@UtilityClass
public class CreateOrderDtoFactory {

    public CreateOrderDto getSimpleCreateOrderDto() {
        return new CreateOrderDto(1, 5);
    }

    public CreateOrderDto getCreateOrderDtoWithInvalidQuantity() {
        return new CreateOrderDto(1, 0);
    }
}
