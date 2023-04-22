package generators.factories;

import generators.factories.users.UserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.User;

import static generators.factories.certificates.GiftCertificateFactory.*;
import static generators.factories.tags.TagFactory.*;

public class PageFactory {

    public static Page<GiftCertificate> getCertificatePage() {
        return new PageImpl<>(getSimpleGiftCertificates());
    }

    public static Page<Tag> getTagPage() {
        return new PageImpl<>(getDifferentTagsList());
    }

    public static Page<User> getUserPage() {
        return new PageImpl<>(UserFactory.getSimpleUsers());
    }
}
