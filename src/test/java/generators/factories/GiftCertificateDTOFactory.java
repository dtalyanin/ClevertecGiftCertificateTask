package generators.factories;

import generators.builders.GiftCertificateDTOBuilder;
import ru.clevertec.ecl.dto.GiftCertificateDTO;

import java.util.List;

import static generators.factories.TagDTOFactory.*;

public class GiftCertificateDTOFactory {

    public static GiftCertificateDTO getSimpleGiftCertificateDTO() {
        return GiftCertificateDTOBuilder.builder()
                .tags(getSimpleTagDTOs())
                .build();
    }

    public static GiftCertificateDTO getGiftCertificateDTOWithoutFields() {
        return GiftCertificateDTOBuilder.builder()
                .name(null)
                .description(null)
                .price(null)
                .duration(null)
                .createDate(null)
                .lastUpdateDate(null)
                .build();
    }

    public static List<GiftCertificateDTO> getSimpleGiftCertificateDTOs() {
        return List.of(getSimpleGiftCertificateDTO(), getSimpleGiftCertificateDTO());
    }
}
