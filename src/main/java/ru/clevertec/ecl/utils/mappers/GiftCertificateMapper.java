package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.exceptions.InvalidItemException;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.codes.ErrorCode;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Mapper(componentModel = "spring",
        uses = TagMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class GiftCertificateMapper {
    private static final BigDecimal COINS = BigDecimal.valueOf(100);

    private final Validator validator;

    public GiftCertificateMapper() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Mapping(source = "price", target = "price", qualifiedByName = "priceInRubles")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "days")
    public abstract GiftCertificateDTO giftCertificateToDTO(GiftCertificate giftCertificate);

    @Mapping(source = "price", target = "price", qualifiedByName = "priceInCoins")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "duration")
    public abstract @Valid GiftCertificate dtoToGiftCertificate(GiftCertificateDTO dto);

    public abstract List<GiftCertificateDTO> allGiftCertificateToDTO(List<GiftCertificate> giftCertificates);

    @Mapping(source = "price", target = "price", qualifiedByName = "priceInCoins")
    @Mapping(source = "duration", target = "duration", qualifiedByName = "duration")
    public abstract GiftCertificate modDTOToGiftCertificate(ModGiftCertificateDTO dto,
                                                                   @MappingTarget GiftCertificate updated);

    @Named("priceInRubles")
    protected BigDecimal convertPriceFromCoinsToPriceInRubles(Long price) {
        BigDecimal rubles = null;
        if (price != null) {
            rubles = BigDecimal.valueOf(price, 2);
        }
        return rubles;
    }

    @Named("priceInCoins")
    protected Long convertPriceFromRublesToPriceInCoins(BigDecimal price) {
        Long coins = null;
        if (price != null) {
            coins = price.multiply(COINS).longValue();
        }
        return coins;
    }

    @Named("days")
    protected Long convertDurationToDays(Duration duration) {
        Long days = null;
        if (duration != null) {
            days = duration.toDays();
        }
        return days;
    }

    @Named("duration")
    protected Duration convertDaysToDuration(Long days) {
        Duration duration = null;
        if (days != null) {
            duration = Duration.ofDays(days);
        }
        return duration;
    }
}
