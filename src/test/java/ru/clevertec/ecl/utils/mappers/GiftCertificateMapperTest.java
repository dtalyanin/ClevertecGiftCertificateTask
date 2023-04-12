package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;
import ru.clevertec.ecl.models.GiftCertificate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static generators.factories.certificates.GiftCertificateDtoFactory.*;
import static generators.factories.certificates.GiftCertificateFactory.*;
import static generators.factories.tags.TagFactory.*;
import static generators.factories.certificates.UpdateGiftCertificateDtoFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class GiftCertificateMapperTest {

    @Autowired
    private GiftCertificateMapper mapper;

    @Test
    void checkGiftCertificateToDtoShouldReturnNull() {
        GiftCertificateDto actual = mapper.convertGiftCertificateToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkGiftCertificateToDtoShouldReturnWithoutFields() {
        GiftCertificate certificate = getGiftCertificateWithoutFields();
        GiftCertificateDto actual = mapper.convertGiftCertificateToDto(certificate);
        GiftCertificateDto expected = getGiftCertificateDtoWithoutFields();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGiftCertificateToDtoShouldReturnCorrectDto() {
        GiftCertificate certificate = getSimpleGiftCertificateWithTags();
        GiftCertificateDto actual = mapper.convertGiftCertificateToDto(certificate);
        GiftCertificateDto expected = getSimpleGiftCertificateDtoWithTags();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkDtoToGiftCertificateShouldReturnNull() {
        GiftCertificate actual = mapper.convertDtoToGiftCertificate(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkDtoToGiftCertificateShouldThrowExceptionInvalidFields() {
        GiftCertificateDto certificateDto = getGiftCertificateDtoWithoutFields();
        assertThatThrownBy(() -> mapper.convertDtoToGiftCertificate(certificateDto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkDtoToGiftCertificateShouldReturnCorrectCertificate() {
        GiftCertificateDto certificateDto = getSimpleGiftCertificateDto();
        GiftCertificate actual = mapper.convertDtoToGiftCertificate(certificateDto);
        GiftCertificate expected = getSimpleGiftCertificateWithNullId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllGiftCertificateToDtoShouldReturnCorrectDtos() {
        List<GiftCertificate> certificates = getSimpleGiftCertificates();
        List<GiftCertificateDto> actual = mapper.convertGiftCertificatesToDtos(certificates);
        List<GiftCertificateDto> expected = getSimpleGiftCertificateDtos();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkUpdateDtoToGiftCertificateShouldNoUpdateBecauseDtoAndTagsIsNull() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        GiftCertificate actual = mapper.updateGiftCertificateFields(null, null, certificate);
        GiftCertificate expected = getSimpleGiftCertificate();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDtoToGiftCertificateShouldNoUpdateBecauseAllDtoFieldAndTagIsNull() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        UpdateGiftCertificateDto updateCertificateDto = getUpdateGiftCertificateDtoWithoutFields();
        GiftCertificate actual = mapper.updateGiftCertificateFields(updateCertificateDto, null, certificate);
        GiftCertificate expected = getSimpleGiftCertificate();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDtoToGiftCertificateShouldUpdateAllField() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        UpdateGiftCertificateDto updateCertificateDto = getSimpleUpdateGiftCertificateDto();
        GiftCertificate actual = mapper
                .updateGiftCertificateFields(updateCertificateDto, getDifferentTagsSet(), certificate);
        GiftCertificate expected = getGiftCertificateWithAllUpdatedFields();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDtoToGiftCertificateShouldUpdateOnlyNamePriceDuration() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        UpdateGiftCertificateDto modCertificateDto = getUpdateGiftCertificateDtoWithOnlyNamePriceDuration();
        GiftCertificate actual = mapper.updateGiftCertificateFields(modCertificateDto, null, certificate);
        GiftCertificate expected = getGiftCertificateWithUpdatedOnlyNamePriceDuration();
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} should convert to {1}")
    @CsvSource({"100, 1.00", "101, 1.01", "999, 9.99", "550, 5.50"})
    void checkConvertPriceFromCoinsToPriceInRubles(Long value, BigDecimal expected) {
        BigDecimal actual = mapper.convertPriceFromCoinsToRubles(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkConvertPriceFromCoinsToPriceInRublesShouldReturnNull() {
        BigDecimal actual = mapper.convertPriceFromCoinsToRubles(null);
        assertThat(actual).isNull();
    }

    @ParameterizedTest(name = "{0} should convert to {1}")
    @CsvSource({"1.00, 100", "1.01, 101", "9.99, 999", "5.50, 550"})
    void checkConvertPriceFromRublesToPriceInCoins(BigDecimal value, Long expected) {
        Long actual = mapper.convertPriceFromRublesToCoins(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkConvertPriceFromRublesToPriceInCoinsShouldReturnNull() {
        Long actual = mapper.convertPriceFromRublesToCoins(null);
        assertThat(actual).isNull();
    }

    @ParameterizedTest(name = "{0} should convert to {1}")
    @CsvSource({"0, 0", "86400, 1", "432000, 5"})
    void checkConvertDurationToDays(Long durationInSeconds, Long expected) {
        Long actual = mapper.convertDurationToDays(Duration.ofSeconds(durationInSeconds));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkConvertDurationToDaysShouldReturnNull() {
        Long actual = mapper.convertDurationToDays(null);
        assertThat(actual).isNull();
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "5"})
    void convertDaysToDuration(Long durationInDays) {
        Duration actual = mapper.convertDaysToDuration(durationInDays);
        Duration expected = Duration.ofDays(durationInDays);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkConvertDaysToDurationShouldReturnNull() {
        Duration actual = mapper.convertDaysToDuration(null);
        assertThat(actual).isNull();
    }
}