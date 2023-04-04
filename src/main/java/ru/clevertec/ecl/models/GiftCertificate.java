package ru.clevertec.ecl.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@Validated
@Entity
@Table(name = "gift_certificates")
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Gift certificate name must contain at least 1 character")
    private String name;
    @NotBlank(message = "Gift certificate description must contain at least 1 character")
    private  String description;
    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @EqualsAndHashCode.Exclude
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

    public GiftCertificate(long id, String name, String description, Set<Tag> tags, Long price, Duration duration,
                           LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getGiftCertificates().add(this);
    }

    public void removeTag(Tag tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
            tag.getGiftCertificates().remove(this);
        }
    }
}
