package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import ru.clevertec.ecl.dto.BaseEntity;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="certificate_id", foreignKey = @ForeignKey(name = "fk_student_teacher",
            foreignKeyDefinition = " /*FOREIGN KEY in sql that sets ON DELETE SET NULL*/"))
    private GiftCertificate certificate;
    @Column(name = "price", nullable = false)
    private Long price;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
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
