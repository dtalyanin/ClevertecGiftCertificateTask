package generators.factories;

import generators.factories.users.UserFactory;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.User;

import static generators.factories.certificates.GiftCertificateFactory.*;
import static generators.factories.tags.TagFactory.*;

@UtilityClass
public class PageFactory {

    public Page<GiftCertificate> getCertificatePage() {
        return new PageImpl<>(getSimpleGiftCertificates());
    }

    public Page<Tag> getTagPage() {
        return new PageImpl<>(getDifferentTagsList());
    }

    public Page<User> getUserPage() {
        return new PageImpl<>(UserFactory.getSimpleUsers());
    }
}
