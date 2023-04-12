package generators.factories.certificates;

import generators.builders.UpdateGiftCertificateDtoBuilder;
import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;

@UtilityClass
public class UpdateGiftCertificateDtoFactory {

    public UpdateGiftCertificateDto getSimpleUpdateGiftCertificateDto() {
        return UpdateGiftCertificateDtoBuilder.builder().build();
    }

    public UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithOnlyNamePriceDuration() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .description(null)
                .tags(null)
                .build();
    }

    public UpdateGiftCertificateDto getUpdateGiftCertificateDtoWithoutFields() {
        return UpdateGiftCertificateDtoBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .build();
    }
}
