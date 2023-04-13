package generators.factories.certificates;

import ru.clevertec.ecl.dto.certificates.GiftCertificateDtoForOrder;

public class GiftCertificateDtoForOrderFactory {

    public static GiftCertificateDtoForOrder getSimpleGiftCertificateDtoForOrder() {
        return new GiftCertificateDtoForOrder("Test", "Test description", 1L);
    }
}
