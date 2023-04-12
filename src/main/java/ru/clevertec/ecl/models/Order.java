package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @NotNull
    private User user;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="certificate_id", foreignKey = @ForeignKey(name = "fk_student_teacher",
            foreignKeyDefinition = " /*FOREIGN KEY in sql that sets ON DELETE SET NULL*/"))
    @NotNull
    private GiftCertificate certificate;
    @Min(1)
    @Column(name = "price", nullable = false)
    private Long price;
    @Column(name = "quantity", nullable = false)
    @Min(1)
    private Integer quantity;
    @Column(name = "total_price", nullable = false)
    @Min(1)
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
