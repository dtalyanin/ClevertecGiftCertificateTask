package generators.factories.certificates;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDtoForOrder;

@UtilityClass
public class GiftCertificateDtoForOrderFactory {

    public GiftCertificateDtoForOrder getSimpleGiftCertificateDtoForOrder() {
        return new GiftCertificateDtoForOrder("Test", "Test description", 1L);
    }
}
