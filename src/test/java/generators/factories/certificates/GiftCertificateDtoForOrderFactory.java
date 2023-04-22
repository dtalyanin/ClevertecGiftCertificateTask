package generators.factories.certificates;

import ru.clevertec.ecl.dto.certificates.GiftCertificateDtoForOrder;

public class GiftCertificateDtoForOrderFactory {

    public static GiftCertificateDtoForOrder getSimpleGiftCertificateDtoForOrder() {
        return new GiftCertificateDtoForOrder("Test", "Test description", 1L);
    }

    public static GiftCertificateDtoForOrder getSimpleGiftCertificateDtoForOrder2() {
        return new GiftCertificateDtoForOrder("Test 2", "Test description 2", 2L);
    }
}
