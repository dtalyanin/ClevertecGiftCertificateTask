package generators.factories;

import generators.builders.GiftCertificateBuilder;
import ru.clevertec.ecl.models.GiftCertificate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static generators.factories.TagFactory.*;

public class GiftCertificateFactory {

    public static GiftCertificate getGiftCertificateWithoutFields() {
        return GiftCertificateBuilder.builder()
                .id(null)
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .createDate(null)
                .lastUpdateDate(null)
                .build();
    }

    public static GiftCertificate getSimpleGiftCertificate() {
        return GiftCertificateBuilder.builder().build();
    }

    public static GiftCertificate getSimpleGiftCertificate2() {
        return GiftCertificateBuilder.builder()
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
        return GiftCertificateBuilder.builder()
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
        return GiftCertificateBuilder.builder()
                .id(0L)
                .tags(getDifferentTagsWithoutId())
                .build();
    }

    public static GiftCertificate getGiftCertificateWithAllUpdatedFields() {
        return GiftCertificateBuilder.builder()
                .tags(new HashSet<>(getDifferentTags()))
                .build();
    }

    public static GiftCertificate getGiftCertificateWithUpdatedOnlyNamePriceDuration() {
        return GiftCertificateBuilder.builder()
                .id(null)
                .description(null)
                .tags(null)
                .createDate(null)
                .lastUpdateDate(null)
                .build();
    }

    public static List<GiftCertificate> getSimpleGiftCertificates() {
        return List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate());
    }
}
