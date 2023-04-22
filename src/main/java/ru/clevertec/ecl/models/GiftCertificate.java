package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "gift_certificates")
public class GiftCertificate implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Gift certificate name must contain at least 1 character")
    private String name;
    @NotBlank(message = "Gift certificate description must contain at least 1 character")
    private  String description;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "gift_certificates_tags",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Min price is 1 coin")
    private Long price;
    @NotNull(message = "Duration cannot be null")
    private Duration duration;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @PrePersist
    public void addCreateAndLastUpdateDate() {
        LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.lastUpdateDate = now;
    }

    @PreUpdate
    public void changeLastUpdateDate() {
        this.lastUpdateDate = LocalDateTime.now();
    }
}
