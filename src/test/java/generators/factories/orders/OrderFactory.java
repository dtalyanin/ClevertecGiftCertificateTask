package generators.factories.orders;

import generators.builders.OrderBuilder;
import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.models.Order;

import java.util.List;

@UtilityClass
public class OrderFactory {

    public Order getSimpleOrder() {
        return OrderBuilder.builder().build();
    }

    public Order getSimpleOrder2() {
        return OrderBuilder.builder()
                .id(2L)
                .quantity(10)
                .totalPrice(10000L)
                .build();
    }

    public Order getSimpleOrderBeforeSaving() {
        return OrderBuilder.builder()
                .id(null)
                .totalPrice(null)
                .orderDate(null)
                .build();
    }

    public List<Order> getDifferentOrders() {
        return List.of(getSimpleOrder(), getSimpleOrder2());
    }
}
