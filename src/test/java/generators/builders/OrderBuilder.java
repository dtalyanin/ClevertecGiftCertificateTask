package generators.builders;

import generators.factories.certificates.GiftCertificateFactory;
import generators.factories.users.UserFactory;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Order;
import ru.clevertec.ecl.models.User;

import java.time.LocalDateTime;

import static generators.factories.certificates.GiftCertificateFactory.*;
import static generators.factories.users.UserFactory.*;


@NoArgsConstructor(staticName = "builder")
public class OrderBuilder {
    private Long id = 1L;
    private User user = getSimpleUser();
    private GiftCertificate certificate = getSimpleGiftCertificate();
    private Long price = 1000L;
    private Integer quantity = 5;
    private Long totalPrice = 5000L;
    private LocalDateTime orderDate = LocalDateTime.of(2023, 4, 1, 1, 0 ,0);

    public OrderBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder user(User user) {
        this.user = user;
        return this;
    }

    public OrderBuilder certificate(GiftCertificate certificate) {
        this.certificate = certificate;
        return this;
    }

    public OrderBuilder price(Long price) {
        this.price = price;
        return this;
    }

    public OrderBuilder quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderBuilder totalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public OrderBuilder orderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public Order build() {
        return new Order(id, user, certificate, price, quantity, totalPrice, orderDate);
    }
}
