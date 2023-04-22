package generators.builders;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;
import ru.clevertec.ecl.dto.TagDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(staticName = "builder")
public class GiftCertificateDtoBuilder {
    private String name = "Test";
    private String description = "Test description";
    private Set<TagDto> tags = new HashSet<>();
    private BigDecimal price = BigDecimal.valueOf(1000, 2);
    private Long duration = 1L;
    private LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 1, 0 ,0);
    private LocalDateTime lastUpdateDate = LocalDateTime.of(2023, 2, 22, 6, 0 ,0);

    public GiftCertificateDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GiftCertificateDtoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public GiftCertificateDtoBuilder tags(Set<TagDto> tags) {
        this.tags = tags;
        return this;
    }

    public GiftCertificateDtoBuilder tag(TagDto tag) {
        tags.add(tag);
        return this;
    }

    public GiftCertificateDtoBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public GiftCertificateDtoBuilder duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public GiftCertificateDtoBuilder createDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public GiftCertificateDtoBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public GiftCertificateDto build() {
        return new GiftCertificateDto(name, description, tags, price, duration, createDate, lastUpdateDate);
    }
}
