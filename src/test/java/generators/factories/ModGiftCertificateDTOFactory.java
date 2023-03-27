package generators.factories;

import generators.builders.ModGiftCertificateDTOBuilder;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;

public class ModGiftCertificateDTOFactory {

    public static ModGiftCertificateDTO getSimpleModGiftCertificateDTO() {
        return ModGiftCertificateDTOBuilder.builder().build();
    }

    public static ModGiftCertificateDTO getModGiftCertificateDTOWithOnlyNamePriceDuration() {
        return ModGiftCertificateDTOBuilder.builder()
                .description(null)
                .tags(null)
                .build();
    }

    public static ModGiftCertificateDTO getModGiftCertificateDTOWithoutFields() {
        return ModGiftCertificateDTOBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .build();
    }
}
