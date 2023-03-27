package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.dto.GiftCertificateDTO;
import ru.clevertec.ecl.dto.ModGiftCertificateDTO;
import ru.clevertec.ecl.models.GiftCertificate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static generators.factories.GiftCertificateDTOFactory.*;
import static generators.factories.GiftCertificateFactory.*;
import static generators.factories.ModGiftCertificateDTOFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class GiftCertificateMapperTest {
    @Autowired
    private GiftCertificateMapper mapper;

    @Test
    void checkGiftCertificateToDTOShouldReturnNull() {
        GiftCertificateDTO actual = mapper.giftCertificateToDTO(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkGiftCertificateToDTOShouldReturnWithoutFields() {
        GiftCertificate certificate = getGiftCertificateWithoutFields();
        GiftCertificateDTO actual = mapper.giftCertificateToDTO(certificate);
        GiftCertificateDTO expected = getGiftCertificateDTOWithoutFields();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGiftCertificateToDTOShouldReturnCorrectDTO() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        GiftCertificateDTO actual = mapper.giftCertificateToDTO(certificate);
        GiftCertificateDTO expected = getSimpleGiftCertificateDTO();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkDTOToGiftCertificateShouldReturnNull() {
        GiftCertificate actual = mapper.dtoToGiftCertificate(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkDTOToGiftCertificateShouldThrowExceptionInvalidFields() {
        GiftCertificateDTO certificateDTO = getGiftCertificateDTOWithoutFields();
        assertThatThrownBy(() -> mapper.dtoToGiftCertificate(certificateDTO))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkDTOToGiftCertificateShouldReturnCorrectCertificate() {
        GiftCertificateDTO certificateDTO = getSimpleGiftCertificateDTO();
        GiftCertificate actual = mapper.dtoToGiftCertificate(certificateDTO);
        GiftCertificate expected = getSimpleGiftCertificateWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllGiftCertificateToDTOShouldReturnCorrectDTOs() {
        List<GiftCertificate> certificates = getSimpleGiftCertificates();
        List<GiftCertificateDTO> actual = mapper.allGiftCertificateToDTO(certificates);
        List<GiftCertificateDTO> expected = getSimpleGiftCertificateDTOs();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldNoUpdateBecauseDTOIsNull() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        GiftCertificate actual = mapper.modDTOToGiftCertificate(null, certificate);
        GiftCertificate expected = getSimpleGiftCertificate();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldNoUpdateBecauseAllFieldIsNull() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        ModGiftCertificateDTO modCertificateDTO = getModGiftCertificateDTOWithoutFields();
        GiftCertificate actual = mapper.modDTOToGiftCertificate(modCertificateDTO, certificate);
        GiftCertificate expected = getSimpleGiftCertificate();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldUpdateAllField() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        ModGiftCertificateDTO modCertificateDTO = getSimpleModGiftCertificateDTO();
        GiftCertificate actual = mapper.modDTOToGiftCertificate(modCertificateDTO, certificate);
        GiftCertificate expected = getGiftCertificateWithAllUpdatedFields();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldUpdateOnlyNamePriceDuration() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        ModGiftCertificateDTO modCertificateDTO = getModGiftCertificateDTOWithOnlyNamePriceDuration();
        GiftCertificate actual = mapper.modDTOToGiftCertificate(modCertificateDTO, certificate);
        GiftCertificate expected = getGiftCertificateWithUpdatedOnlyNamePriceDuration();
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} should convert to {1}")
    @CsvSource({"100, 1.00", "101, 1.01", "999, 9.99", "550, 5.50"})
    void checkConvertPriceFromCoinsToPriceInRubles(Long value, BigDecimal expected) {
        BigDecimal actual = mapper.convertPriceFromCoinsToPriceInRubles(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkConvertPriceFromCoinsToPriceInRublesShouldReturnNull() {
        BigDecimal actual = mapper.convertPriceFromCoinsToPriceInRubles(null);
        assertThat(actual).isNull();
    }

    @ParameterizedTest(name = "{0} should convert to {1}")
    @CsvSource({"1.00, 100", "1.01, 101", "9.99, 999", "5.50, 550"})
    void checkConvertPriceFromRublesToPriceInCoins(BigDecimal value, Long expected) {
        Long actual = mapper.convertPriceFromRublesToPriceInCoins(value);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkConvertPriceFromRublesToPriceInCoinsShouldReturnNull() {
        Long actual = mapper.convertPriceFromRublesToPriceInCoins(null);
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