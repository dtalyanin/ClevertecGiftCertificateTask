package generators.factories.certificates;

import generators.builders.UpdateGiftCertificateDtoBuilder;
import ru.clevertec.ecl.dto.certificates.UpdateGiftCertificateDto;

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
}
