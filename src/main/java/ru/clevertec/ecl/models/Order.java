package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "orders")
public class Order implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private GiftCertificate certificate;
    @Min(value = 1, message = "Min price is 1 coin")
    @Column(name = "price", nullable = false)
    private Long price;
    @Min(value = 1, message = "Min quantity is 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Min(value = 1, message = "Min total price is 1 coin")
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @PrePersist
    public void setTotalPriceAndDate() {
        orderDate = LocalDateTime.now();
        price = certificate.getPrice();
        totalPrice = price * quantity;
    }
}
