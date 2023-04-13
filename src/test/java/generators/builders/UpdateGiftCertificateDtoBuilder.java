package generators.builders;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.dto.certificates.UpdateGiftCertificateDto;
import ru.clevertec.ecl.dto.TagDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(staticName = "builder")
public class UpdateGiftCertificateDtoBuilder {
    private String name = "Test mod";
    private String description = "Test description mod";
    private List<TagDto> tags = new ArrayList<>();
    private BigDecimal price = BigDecimal.valueOf(2000, 2);
    private Long duration = 2L;

    public UpdateGiftCertificateDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UpdateGiftCertificateDtoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public UpdateGiftCertificateDtoBuilder tags(List<TagDto> tags) {
        this.tags = tags;
        return this;
    }

    public UpdateGiftCertificateDtoBuilder tag(TagDto tag) {
        tags.add(tag);
        return this;
    }

    public UpdateGiftCertificateDtoBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public UpdateGiftCertificateDtoBuilder duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public UpdateGiftCertificateDto build() {
        return new UpdateGiftCertificateDto(name, description, tags, price, duration);
    }
}
