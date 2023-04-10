package generators.factories;

import generators.builders.ModGiftCertificateDTOBuilder;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;

public class ModGiftCertificateDTOFactory {

    public static UpdateGiftCertificateDto getSimpleModGiftCertificateDTO() {
        return ModGiftCertificateDTOBuilder.builder().build();
    }

    public static UpdateGiftCertificateDto getModGiftCertificateDTOWithOnlyNamePriceDuration() {
        return ModGiftCertificateDTOBuilder.builder()
                .description(null)
                .tags(null)
                .build();
    }

    public static UpdateGiftCertificateDto getModGiftCertificateDTOWithoutFields() {
        return ModGiftCertificateDTOBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .build();
    }
}
