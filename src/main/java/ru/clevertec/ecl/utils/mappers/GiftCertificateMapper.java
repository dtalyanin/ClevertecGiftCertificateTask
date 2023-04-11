package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.Valid;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.GiftCertificateDtoForOrder;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;
import ru.clevertec.ecl.models.GiftCertificate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Validated
@Mapper(componentModel = "spring",
        uses = TagMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class GiftCertificateMapper {

    private static final BigDecimal COINS = BigDecimal.valueOf(100);
    private static final int COINS_IN_RUBLE = 2;

    @Mapping(source = "price", target = "price", qualifiedByName = "fromCoinsToRubles")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "durationToDays")
    public abstract GiftCertificateDto convertGiftCertificateToDto(GiftCertificate giftCertificate);

    public abstract List<GiftCertificateDto> convertGiftCertificatesToDtos(List<GiftCertificate> giftCertificates);

    @Mapping(source = "price", target = "price", qualifiedByName = "fromRublesToCoins")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "daysToDuration")
    public abstract @Valid GiftCertificate convertDtoToGiftCertificate(GiftCertificateDto dto);

    @Mapping(source = "price", target = "price", qualifiedByName = "fromRublesToCoins")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "daysToDuration")
    public abstract GiftCertificate convertUpdateDtoToGiftCertificate(UpdateGiftCertificateDto dto);

    public abstract @Valid GiftCertificate updateGiftCertificateFields(GiftCertificate from,
                                                                       @MappingTarget GiftCertificate to);

    @Mapping(source = "duration", target = "duration", qualifiedByName = "durationToDays")
    public abstract GiftCertificateDtoForOrder convertGiftCertificatesToDtoForOrder(GiftCertificate giftCertificate);

    @Named("fromCoinsToRubles")
    protected BigDecimal convertPriceFromCoinsToRubles(Long price) {
        BigDecimal rubles = null;
        if (price != null) {
            rubles = BigDecimal.valueOf(price, COINS_IN_RUBLE);
        }
        return rubles;
    }

    @Named("fromRublesToCoins")
    protected Long convertPriceFromRublesToCoins(BigDecimal price) {
        Long coins = null;
        if (price != null) {
            coins = price.multiply(COINS).longValue();
        }
        return coins;
    }

    @Named("durationToDays")
    protected Long convertDurationToDays(Duration duration) {
        Long days = null;
        if (duration != null) {
            days = duration.toDays();
        }
        return days;
    }

    @Named("daysToDuration")
    protected Duration convertDaysToDuration(Long days) {
        Duration duration = null;
        if (days != null) {
            duration = Duration.ofDays(days);
        }
        return duration;
    }
}
