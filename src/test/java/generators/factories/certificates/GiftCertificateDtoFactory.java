package generators.factories.certificates;

import generators.builders.GiftCertificateDtoBuilder;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static generators.factories.tags.TagDtoFactory.*;

public class GiftCertificateDtoFactory {

    public static GiftCertificateDto getSimpleGiftCertificateDto() {
        return GiftCertificateDtoBuilder.builder()
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDtoWithTags() {
        return GiftCertificateDtoBuilder.builder()
                .tags(getSimpleTagDtosSet())
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDto2() {
        return GiftCertificateDtoBuilder.builder()
                .name("Test 2")
                .description("Test description 2")
                .tags(Set.of(getSimpleTagDto(), getSimpleTagDto2()))
                .price(BigDecimal.valueOf(2000, 2))
                .duration(2L)
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDto3() {
        return GiftCertificateDtoBuilder.builder()
                .name("Test 3")
                .description("Test description 3")
                .tags(Collections.emptySet())
                .price(BigDecimal.valueOf(3000, 2))
                .duration(3L)
                .createDate(LocalDateTime.of(2023, 1, 3, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDto getGiftCertificateDtoWithoutFields() {
        return GiftCertificateDtoBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .createDate(null)
                .lastUpdateDate(null)
                .build();
    }

    public static List<GiftCertificateDto> getSimpleGiftCertificateDtos() {
        return List.of(getSimpleGiftCertificateDto(), getSimpleGiftCertificateDto2(), getSimpleGiftCertificateDto3());
    }
}
