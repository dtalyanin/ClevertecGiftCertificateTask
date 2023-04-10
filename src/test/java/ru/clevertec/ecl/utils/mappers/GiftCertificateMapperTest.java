package ru.clevertec.ecl.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;
import ru.clevertec.ecl.exceptions.InvalidItemException;
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

    private GiftCertificateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(GiftCertificateMapper.class);
    }

    @Test
    void checkGiftCertificateToDTOShouldReturnNull() {
        GiftCertificateDto actual = mapper.convertGiftCertificateToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkGiftCertificateToDTOShouldReturnWithoutFields() {
        GiftCertificate certificate = getGiftCertificateWithoutFields();
        GiftCertificateDto actual = mapper.convertGiftCertificateToDto(certificate);
        GiftCertificateDto expected = getGiftCertificateDTOWithoutFields();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkGiftCertificateToDTOShouldReturnCorrectDTO() {
        GiftCertificate certificate = getSimpleGiftCertificateWithTags();
        GiftCertificateDto actual = mapper.convertGiftCertificateToDto(certificate);
        GiftCertificateDto expected = getSimpleGiftCertificateDTO();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkDTOToGiftCertificateShouldReturnNull() {
        GiftCertificate actual = mapper.convertDtoToGiftCertificate(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkDTOToGiftCertificateShouldThrowExceptionInvalidFields() {
        GiftCertificateDto certificateDTO = getGiftCertificateDTOWithoutFields();
        assertThatThrownBy(() -> mapper.convertDtoToGiftCertificate(certificateDTO))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void checkDTOToGiftCertificateShouldReturnCorrectCertificate() {
        GiftCertificateDto certificateDTO = getSimpleGiftCertificateDTO();
        GiftCertificate actual = mapper.convertDtoToGiftCertificate(certificateDTO);
        GiftCertificate expected = getSimpleGiftCertificateWithNullId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllGiftCertificateToDTOShouldReturnCorrectDTOs() {
        List<GiftCertificate> certificates = getSimpleGiftCertificates();
        List<GiftCertificateDto> actual = mapper.convertGiftCertificatesToDtos(certificates);
        List<GiftCertificateDto> expected = getSimpleGiftCertificateDTOs();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldNoUpdateBecauseDTOIsNull() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        GiftCertificate actual = mapper.convertUpdateDtoToGiftCertificate(null, certificate);
        GiftCertificate expected = getSimpleGiftCertificate();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldNoUpdateBecauseAllFieldIsNull() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        UpdateGiftCertificateDto modCertificateDTO = getModGiftCertificateDTOWithoutFields();
        GiftCertificate actual = mapper.convertUpdateDtoToGiftCertificate(modCertificateDTO, certificate);
        GiftCertificate expected = getSimpleGiftCertificate();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldUpdateAllField() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        UpdateGiftCertificateDto modCertificateDTO = getSimpleModGiftCertificateDTO();
        GiftCertificate actual = mapper.convertUpdateDtoToGiftCertificate(modCertificateDTO, certificate);
        GiftCertificate expected = getGiftCertificateWithAllUpdatedFields();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkModDTOToGiftCertificateShouldUpdateOnlyNamePriceDuration() {
        GiftCertificate certificate = getSimpleGiftCertificate();
        UpdateGiftCertificateDto modCertificateDTO = getModGiftCertificateDTOWithOnlyNamePriceDuration();
        GiftCertificate actual = mapper.convertUpdateDtoToGiftCertificate(modCertificateDTO, certificate);
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