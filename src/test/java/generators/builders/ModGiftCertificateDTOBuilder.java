package generators.builders;

import lombok.NoArgsConstructor;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.dto.TagDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(staticName = "builder")
public class ModGiftCertificateDTOBuilder {
    private String name = "Test mod";
    private String description = "Test description mod";
    private List<TagDTO> tags = new ArrayList<>();
    private BigDecimal price = BigDecimal.valueOf(2000, 2);
    private Long duration = 2L;

    public ModGiftCertificateDTOBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ModGiftCertificateDTOBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ModGiftCertificateDTOBuilder tags(List<TagDTO> tags) {
        this.tags = tags;
        return this;
    }

    public ModGiftCertificateDTOBuilder tag(TagDTO tag) {
        tags.add(tag);
        return this;
    }

    public ModGiftCertificateDTOBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ModGiftCertificateDTOBuilder duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public ModGiftCertificateDTO build() {
        return new ModGiftCertificateDTO(name, description, tags, price, duration);
    }
}
