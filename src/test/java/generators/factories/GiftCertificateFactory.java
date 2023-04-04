package generators.factories;

import ru.clevertec.ecl.models.GiftCertificate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static generators.factories.TagFactory.*;

public class GiftCertificateFactory {

    public static GiftCertificate getGiftCertificateWithoutFields() {
        return GiftCertificate.builder().build();
    }

    public static GiftCertificate getSimpleGiftCertificate() {
        return GiftCertificate.builder()
                .id(1L)
                .name("Test")
                .description("Test description")
                .tags(getDifferentTags())
                .price(1000L)
                .duration(Duration.ofDays(1))
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificate getSimpleGiftCertificate2() {
        return GiftCertificate.builder()
                .id(2L)
                .name("Test 2")
                .description("Test description 2")
                .tags(Set.of(getSimpleTag(), getSimpleTag2()))
                .price(2000L)
                .duration(Duration.ofDays(2))
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificate getSimpleGiftCertificate3() {
        return GiftCertificate.builder()
                .id(3L)
                .name("Test 3")
                .description("Test description 3")
                .tags(Collections.emptySet())
                .price(3000L)
                .duration(Duration.ofDays(3))
                .createDate(LocalDateTime.of(2023, 1, 3, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificate getSimpleGiftCertificateWithoutId() {
        return GiftCertificate.builder()
                .id(0L)
                .name("Test")
                .description("Test description")
                .tags(getDifferentTagsWithoutId())
                .price(1000L)
                .duration(Duration.ofDays(1))
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificate getGiftCertificateWithAllUpdatedFields() {
        return GiftCertificate.builder()
                .id(1L)
                .name("Test mod")
                .description("Test description mod")
                .tags(new ArrayList<>())
                .price(2000L)
                .duration(Duration.ofDays(2))
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificate getGiftCertificateWithUpdatedOnlyNamePriceDuration() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        certificate.setName("Test mod");
        certificate.setPrice(2000L);
        certificate.setDuration(Duration.ofDays(2));
        return certificate;
    }

    public static List<GiftCertificate> getSimpleGiftCertificates() {
        return List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate());
    }
}
