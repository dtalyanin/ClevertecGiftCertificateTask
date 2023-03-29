package ru.clevertec.ecl.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.configuration.TestConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
@Rollback
class GitCertificatesTagsDAOImplTest {
    @Autowired
    private GitCertificatesTagsDAOImpl dao;

    @Test
    void checkAddGiftCertificateTagsShouldAdd3Rows() {
        int actualUpdatedRows = dao.addGiftCertificateTags(3L, List.of(1L, 2L, 3L));
        int expectedUpdatedRows = 3;
        assertThat(actualUpdatedRows).isEqualTo(expectedUpdatedRows);
    }

    @Test
    void checkDeleteGiftCertificateTagsShouldDelete3Rows() {
        int actualDeletedRows = dao.deleteGiftCertificateTags(1L);
        int expectedDeletedRows = 3;
        assertThat(actualDeletedRows).isEqualTo(expectedDeletedRows);
    }

    @Test
    void checkDeleteGiftCertificateTagsShouldNotDeletedRows() {
        int actualDeletedRows = dao.deleteGiftCertificateTags(4L);
        int expectedDeletedRows = 0;
        assertThat(actualDeletedRows).isEqualTo(expectedDeletedRows);
    }
}