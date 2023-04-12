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

    private static final int COINS_RUBLE_SCALE = 2;
    private static final BigDecimal COINS = BigDecimal.valueOf(100);

    @Mapping(source = "price", target = "price")
    @Mapping(source = "duration", target = "duration")
    public abstract GiftCertificateDto convertGiftCertificateToDto(GiftCertificate giftCertificate);

    public abstract List<GiftCertificateDto> convertGiftCertificatesToDtos(List<GiftCertificate> giftCertificates);

    @Mapping(source = "price", target = "price")
    @Mapping(source = "duration", target = "duration")
    public abstract @Valid GiftCertificate convertDtoToGiftCertificate(GiftCertificateDto dto);

    @Mapping(source = "price", target = "price")
    @Mapping(source = "duration", target = "duration")
    public abstract GiftCertificate convertUpdateDtoToGiftCertificate(UpdateGiftCertificateDto dto);

    public abstract @Valid GiftCertificate updateGiftCertificateFields(GiftCertificate from,
                                                                       @MappingTarget GiftCertificate to);

    @Mapping(source = "duration", target = "duration")
    public abstract GiftCertificateDtoForOrder convertGiftCertificatesToDtoForOrder(GiftCertificate giftCertificate);
    
    protected BigDecimal convertPriceFromCoinsToRubles(Long price) {
        BigDecimal rubles = null;
        if (price != null) {
            rubles = BigDecimal.valueOf(price, COINS_RUBLE_SCALE);
        }
        return rubles;
    }
    
    protected Long convertPriceFromRublesToCoins(BigDecimal price) {
        Long coins = null;
        if (price != null) {
            coins = price.multiply(COINS).longValue();
        }
        return coins;
    }
    
    protected Long convertDurationToDays(Duration duration) {
        Long days = null;
        if (duration != null) {
            days = duration.toDays();
        }
        return days;
    }
    
    protected Duration convertDaysToDuration(Long duration) {
        Duration durationFromDays = null;
        if (duration != null) {
            durationFromDays = Duration.ofDays(duration);
        }
        return durationFromDays;
    }
}
