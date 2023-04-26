package generators.builders;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(staticName = "builder")
public class GiftCertificateBuilder {
    private Long id = 1L;
    private String name = "Test";
    private String description = "Test description";
    private Set<Tag> tags = new HashSet<>();
    private Long price = 1000L;
    private Duration duration = Duration.ofDays(1);
    private LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 1, 0 ,0);
    private LocalDateTime lastUpdateDate = LocalDateTime.of(2023, 2, 22, 6, 0 ,0);


    public GiftCertificateBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public GiftCertificateBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GiftCertificateBuilder description(String description) {
        this.description = description;
        return this;
    }

    public GiftCertificateBuilder tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public GiftCertificateBuilder tag(Tag tag) {
        tags.add(tag);
        return this;
    }

    public GiftCertificateBuilder price(Long price) {
        this.price = price;
        return this;
    }

    public GiftCertificateBuilder duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public GiftCertificateBuilder createDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public GiftCertificateBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public GiftCertificate build() {
        return new GiftCertificate(id, name, description, tags, price, duration, createDate, lastUpdateDate);
    }
}
