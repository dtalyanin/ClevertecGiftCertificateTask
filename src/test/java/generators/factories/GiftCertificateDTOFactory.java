package generators.factories;

import generators.builders.GiftCertificateDTOBuilder;
import ru.clevertec.ecl.dto.GiftCertificateDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static generators.factories.TagDTOFactory.*;

public class GiftCertificateDTOFactory {

    public static GiftCertificateDTO getSimpleGiftCertificateDTO() {
        return GiftCertificateDTOBuilder.builder()
                .tags(getSimpleTagDTOs())
                .build();
    }

    public static GiftCertificateDTO getSimpleGiftCertificateDTO2() {
        return GiftCertificateDTOBuilder.builder()
                .name("Test 2")
                .description("Test description 2")
                .tags(List.of(getSimpleTagDTO(), getSimpleTagDTO2()))
                .price(BigDecimal.valueOf(2000, 2))
                .duration(2L)
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDTO getSimpleGiftCertificateDTO3() {
        return GiftCertificateDTOBuilder.builder()
                .name("Test 3")
                .description("Test description 3")
                .tags(Collections.emptyList())
                .price(BigDecimal.valueOf(3000, 2))
                .duration(3L)
                .createDate(LocalDateTime.of(2023, 1, 3, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDTO getGiftCertificateDTOWithoutFields() {
        return GiftCertificateDTOBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .createDate(null)
                .lastUpdateDate(null)
                .build();
    }

    public static List<GiftCertificateDTO> getSimpleGiftCertificateDTOs() {
        return List.of(getSimpleGiftCertificateDTO(), getSimpleGiftCertificateDTO2(), getSimpleGiftCertificateDTO3());
    }
}
