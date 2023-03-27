package generators.builders;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(staticName = "builder")
public class GiftCertificateDTOBuilder {
    private String name = "Test";
    private String description = "Test description";
    private List<TagDTO> tags = new ArrayList<>();
    private BigDecimal price = BigDecimal.valueOf(1000, 2);
    private Long duration = 1L;
    private LocalDateTime createDate = LocalDateTime.of(2023, 1, 1, 1, 0 ,0);
    private LocalDateTime lastUpdateDate = LocalDateTime.of(2023, 2, 22, 6, 0 ,0);

    public GiftCertificateDTOBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GiftCertificateDTOBuilder description(String description) {
        this.description = description;
        return this;
    }

    public GiftCertificateDTOBuilder tags(List<TagDTO> tags) {
        this.tags = tags;
        return this;
    }

    public GiftCertificateDTOBuilder tag(TagDTO tag) {
        tags.add(tag);
        return this;
    }

    public GiftCertificateDTOBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public GiftCertificateDTOBuilder duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public GiftCertificateDTOBuilder createDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public GiftCertificateDTOBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public GiftCertificateDTO build() {
        return new GiftCertificateDTO(name, description, tags, price, duration, createDate, lastUpdateDate);
    }
}
