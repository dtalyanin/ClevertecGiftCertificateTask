package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;
    @ManyToOne
    @JoinColumn(name="certificate_id")
    private GiftCertificate certificate;
    @Column(name = "price", nullable = false)
    private Long price;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime date;

    @PrePersist
    public void setPriceAndDate() {
        date = LocalDateTime.now();
        price = certificate.getPrice();
        totalPrice = price * quantity;
    }
}
