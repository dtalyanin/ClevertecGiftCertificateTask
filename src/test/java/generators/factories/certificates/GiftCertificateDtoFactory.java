package generators.factories.certificates;

import generators.builders.GiftCertificateDtoBuilder;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static generators.factories.tags.TagDtoFactory.*;

public class GiftCertificateDtoFactory {

    public static GiftCertificateDto getSimpleGiftCertificateDto() {
        return GiftCertificateDtoBuilder.builder()
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDtoWithTags() {
        return GiftCertificateDtoBuilder.builder()
                .tags(getSimpleTagDtosSet())
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDtoWithTagsAsInDb() {
        return GiftCertificateDtoBuilder.builder()
                .tags(getSimpleTagDtosSetAsInDb())
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDtoWithTagsAsInDb2() {
        return GiftCertificateDtoBuilder.builder()
                .name("Test 2")
                .description("Test description 2")
                .tags(getSimpleTagDtosSetAsInDb2())
                .price(BigDecimal.valueOf(2000, 2))
                .duration(2L)
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDto2() {
        return GiftCertificateDtoBuilder.builder()
                .name("Test 2")
                .description("Test description 2")
                .tags(Set.of(getSimpleTagDto(), getSimpleTagDto2()))
                .price(BigDecimal.valueOf(2000, 2))
                .duration(2L)
                .createDate(LocalDateTime.of(2023, 1, 1, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDto getSimpleGiftCertificateDto3() {
        return GiftCertificateDtoBuilder.builder()
                .name("Test 3")
                .description("Test description 3")
                .tags(Collections.emptySet())
                .price(BigDecimal.valueOf(3000, 2))
                .duration(3L)
                .createDate(LocalDateTime.of(2023, 1, 3, 1, 0 ,0))
                .lastUpdateDate(LocalDateTime.of(2023, 2, 22, 6, 0 ,0))
                .build();
    }

    public static GiftCertificateDto getGiftCertificateDtoWithoutFields() {
        return GiftCertificateDtoBuilder.builder()
                .name(null)
                .description(null)
                .tags(null)
                .price(null)
                .duration(null)
                .createDate(null)
                .lastUpdateDate(null)
                .build();
    }

    public static List<GiftCertificateDto> getSimpleGiftCertificateDtos() {
        return List.of(getSimpleGiftCertificateDto(), getSimpleGiftCertificateDto2(), getSimpleGiftCertificateDto3());
    }

    public static List<GiftCertificateDto> getGiftCertificateDtosFromDb() {
        return List.of(getSimpleGiftCertificateDtoWithTags(), getSimpleGiftCertificateDto2(), getSimpleGiftCertificateDto3());
    }


    public static List<GiftCertificateDto> getEmptyListGiftCertificateDtos() {
        return Collections.emptyList();
    }

    public static List<GiftCertificateDto> getGiftCertificateDtosFromDbWithSize2() {
        return List.of(getSimpleGiftCertificateDtoWithTags(), getSimpleGiftCertificateDto2());
    }

    public static List<GiftCertificateDto> getOneGiftCertificateDtosAfterFiltering() {
        return List.of(getSimpleGiftCertificateDto2());
    }

    public static List<GiftCertificateDto> getOneGiftCertificateDtosAfterFilteringByTags() {
        return List.of(getSimpleGiftCertificateDtoWithTags());
    }

    public static GiftCertificateDto getSimpleGiftCertificateDtoToCreate() {
        return GiftCertificateDtoBuilder.builder()
                .name("Test created")
                .description("Test description created")
                .tags(Set.of(getSimpleTagDto(), getSimpleTagDto2()))
                .price(BigDecimal.valueOf(2000, 2))
                .duration(2L)
                .build();
    }

    public static GiftCertificateDto getGiftCertificateDtoWithEmptyName() {
        return GiftCertificateDtoBuilder.builder()
                .name("")
                .build();
    }

    public static GiftCertificateDto getGiftCertificateDtoWithEmptyDescription() {
        return GiftCertificateDtoBuilder.builder()
                .description("")
                .build();
    }

    public static GiftCertificateDto getGiftCertificateDtoWithZeroPrice() {
        return GiftCertificateDtoBuilder.builder()
                .price(BigDecimal.ZERO)
                .build();
    }

    public static GiftCertificateDto getGiftCertificateDtoWithZeroDuration() {
        return GiftCertificateDtoBuilder.builder()
                .duration(0L)
                .build();
    }

    public static List<GiftCertificateDto> getGiftCertificateDtosFromDbSortByNameOrCrateDateAsc() {
        return List.of(getSimpleGiftCertificateDtoWithTagsAsInDb(), getSimpleGiftCertificateDtoWithTagsAsInDb2(),
                getSimpleGiftCertificateDto3());
    }

    public static List<GiftCertificateDto> getGiftCertificateDtosFromDbSortByNameDesc() {
        return List.of(getSimpleGiftCertificateDto3(), getSimpleGiftCertificateDtoWithTagsAsInDb2(),
                getSimpleGiftCertificateDtoWithTagsAsInDb());
    }

    public static List<GiftCertificateDto> getGiftCertificateDtosFromDbSortByCreateDateDesc() {
        return List.of(getSimpleGiftCertificateDto3(), getSimpleGiftCertificateDtoWithTagsAsInDb(),
                getSimpleGiftCertificateDtoWithTagsAsInDb2());
    }

    public static List<GiftCertificateDto> getGiftCertificateDtosFromDbSortByCreateDateDescAndNameDesc() {
        return List.of(getSimpleGiftCertificateDto3(), getSimpleGiftCertificateDtoWithTagsAsInDb2(),
                getSimpleGiftCertificateDtoWithTagsAsInDb());
    }
}
