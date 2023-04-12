package generators.builders;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.dto.GiftCertificateDtoForOrder;
import ru.clevertec.ecl.dto.OrderDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static generators.factories.certificates.GiftCertificateDtoForOrderFactory.*;

@NoArgsConstructor(staticName = "builder")
public class OrderDtoBuilder {
    private GiftCertificateDtoForOrder certificate = getSimpleGiftCertificateDtoForOrder();
    private BigDecimal price = BigDecimal.valueOf(1000L, 2);
    private Integer quantity = 5;
    private BigDecimal totalPrice = BigDecimal.valueOf(5000L, 2);;
    private LocalDateTime orderDate = LocalDateTime.of(2023, 4, 1, 1, 0 ,0);

    public OrderDtoBuilder certificate(GiftCertificateDtoForOrder certificate) {
        this.certificate = certificate;
        return this;
    }

    public OrderDtoBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public OrderDtoBuilder quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderDtoBuilder totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public OrderDtoBuilder orderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public OrderDto build() {
        return new OrderDto(certificate, price, quantity, totalPrice, orderDate);
    }
}
