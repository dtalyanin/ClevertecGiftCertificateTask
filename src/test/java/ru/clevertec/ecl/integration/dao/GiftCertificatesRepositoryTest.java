package ru.clevertec.ecl.integration.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.GiftCertificatesRepository;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.GiftCertificate;

import java.util.Optional;

import static generators.factories.ExampleFactory.getExampleOfExistingCertificate;
import static generators.factories.ExampleFactory.getExampleOfNotExistingCertificate;
import static generators.factories.certificates.GiftCertificateFactory.getSimpleGiftCertificate;
import static org.assertj.core.api.Assertions.assertThat;

class GiftCertificatesRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private GiftCertificatesRepository repository;

    @Test
    void checkFindByIdShouldReturnCertificate() {
        Optional<GiftCertificate> actualOptionalCertificate = repository.findById(1L);
        GiftCertificate expectedCertificate = getSimpleGiftCertificate();
        assertThat(actualOptionalCertificate)
                .isPresent()
                .hasValue(expectedCertificate);
    }

    @Test
    void checkFindByIdShouldReturnEmptyOptional() {
        Optional<GiftCertificate> actualOptionalCertificate = repository.findById(10L);
        assertThat(actualOptionalCertificate).isEmpty();
    }

    @Test
    void checkFindWithoutTagsByIdShouldReturnCertificate() {
        Optional<GiftCertificate> actualOptionalCertificate = repository.findWithoutTagsById(1L);
        GiftCertificate expectedCertificate = getSimpleGiftCertificate();
        assertThat(actualOptionalCertificate)
                .isPresent()
                .hasValue(expectedCertificate);
    }

    @Test
    void checkFindWithoutTagsByIdShouldReturnEmptyOptional() {
        Optional<GiftCertificate> actualOptionalCertificate = repository.findWithoutTagsById(10L);
        assertThat(actualOptionalCertificate).isEmpty();
    }

    @Test
    void checkExistsShouldReturnTrue() {
        boolean actualExist = repository.exists(getExampleOfExistingCertificate());
        assertThat(actualExist).isTrue();
    }

    @Test
    void checkExistsShouldReturnFalse() {
        boolean actualExist = repository.exists(getExampleOfNotExistingCertificate());
        assertThat(actualExist).isFalse();
    }

    @Test
    void checkDeleteByIdShouldReturn1AffectedRowsCertificateExist() {
        int actualAffectedRows = repository.deleteById(3L);
        int expectedAffectedRows = 1;
        assertThat(actualAffectedRows).isEqualTo(expectedAffectedRows);
    }

    @Test
    void checkDeleteByIdShouldReturn0AffectedRowsCertificateNotExist() {
        int actualAffectedRows = repository.deleteById(10L);
        assertThat(actualAffectedRows).isZero();
    }
}