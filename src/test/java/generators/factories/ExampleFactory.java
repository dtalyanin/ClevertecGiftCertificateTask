package generators.factories;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import ru.clevertec.ecl.models.GiftCertificate;

import static generators.factories.certificates.GiftCertificateFactory.*;
import static ru.clevertec.ecl.utils.constants.GiftCertificateParams.*;

public class ExampleFactory {

    private static final ExampleMatcher MATCHER = ExampleMatcher.matching().withIgnorePaths(CERTIFICATE_ID,
            CERTIFICATE_CREATE_DATE, CERTIFICATE_LAST_UPDATE_DATE);

    public static Example<GiftCertificate> getExampleOfExistingCertificate() {
        return Example.of(getSimpleGiftCertificate(), MATCHER);
    }

    public static Example<GiftCertificate> getExampleOfNotExistingCertificate() {
        return Example.of(getGiftCertificateNotExistingInDb(), MATCHER);
    }
}
