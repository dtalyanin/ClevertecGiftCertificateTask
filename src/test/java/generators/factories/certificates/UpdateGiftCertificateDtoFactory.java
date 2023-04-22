package generators.factories.certificates;

import generators.builders.UpdateGiftCertificateDtoBuilder;
import ru.clevertec.ecl.dto.certificates.UpdateGiftCertificateDto;

import java.math.BigDecimal;

public class UpdateGiftCertificateDtoFactory {

    public static UpdateGiftCertificateDto getSimpleUpdateGiftCertificateDto() {
        return UpdateGiftCertificateDtoBuilder.builder().build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithOnlyNamePriceDuration() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .description(null)
                .tags(null)
                .build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithoutFields() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithEmptyName() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .name("")
                .build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithEmptyDescription() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .description("")
                .build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithZeroPrice() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .price(BigDecimal.ZERO)
                .build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithZeroDuration() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .duration(0L)
                .build();
    }

    public static UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithExistingInDbFields() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .name("Test")
                .description("Test description")
                .price(BigDecimal.valueOf(1000, 2))
                .duration(1L)
                .build();
    }
}
