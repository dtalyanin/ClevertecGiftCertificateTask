package generators.factories.certificates;

import generators.builders.GiftCertificateBuilder;
import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.models.GiftCertificate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static generators.factories.tags.TagFactory.*;

@UtilityClass
public class GiftCertificateFactory {

    public GiftCertificate getGiftCertificateWithoutFields() {
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

    public GiftCertificate getSimpleGiftCertificate() {
        return GiftCertificateBuilder.builder().build();
    }

    public GiftCertificate getSimpleGiftCertificateWithTags() {
        return GiftCertificateBuilder.builder()
                .tags(getDifferentTagsSet())
                .build();
    }

    public GiftCertificate getSimpleGiftCertificate2() {
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

    public GiftCertificate getSimpleGiftCertificate3() {
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

    public GiftCertificate getSimpleGiftCertificateWithoutId() {
        return GiftCertificateBuilder.builder()
                .id(0L)
                .tags(getDifferentTagsWithoutIdSet())
                .build();
    }

    public GiftCertificate getSimpleGiftCertificateWithNullId() {
        return GiftCertificateBuilder.builder()
                .id(null)
                .tags(getDifferentTagsWithoutIdSet())
                .build();
    }

    public GiftCertificate getGiftCertificateWithAllUpdatedFields() {
        return GiftCertificateBuilder.builder()
                .name("Test mod")
                .description("Test description mod")
                .tags(getDifferentTagsSet())
                .price(2000L)
                .duration(Duration.ofDays(2))
                .build();
    }


    public GiftCertificate getGiftCertificateForUpdate() {
        return GiftCertificateBuilder.builder()
                .name("New test")
                .tags(Collections.emptySet())
                .build();
    }

    public GiftCertificate getGiftCertificateWithUpdatedOnlyNamePriceDuration() {
        return GiftCertificateBuilder.builder()
                .name("Test mod")
                .price(2000L)
                .duration(Duration.ofDays(2))
                .build();
    }

    public List<GiftCertificate> getSimpleGiftCertificates() {
        return List.of(getSimpleGiftCertificate(), getSimpleGiftCertificate2(), getSimpleGiftCertificate3());
    }
}
