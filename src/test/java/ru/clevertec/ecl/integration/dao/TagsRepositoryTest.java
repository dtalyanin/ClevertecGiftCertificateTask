package ru.clevertec.ecl.integration.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.TagsRepository;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.Tag;

import java.util.Optional;

import static generators.factories.tags.TagFactory.getSimpleTag;
import static generators.factories.tags.TagFactory.getSimpleTag2;
import static org.assertj.core.api.Assertions.assertThat;

class TagsRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TagsRepository repository;

    @Test
    void checkFindByNameShouldReturnTag() {
        Optional<Tag> actualOptionalTag = repository.findByName("Test tag");
        Tag expectedTag = getSimpleTag();

        assertThat(actualOptionalTag)
                .isPresent()
                .hasValue(expectedTag);
    }

    @Test
    void checkFindByNameShouldReturnEmptyOptionalNotExist() {
        Optional<Tag> actualTag = repository.findByName("Test tag not exist");
        assertThat(actualTag).isEmpty();
    }

    @Test
    void checkFindByNameShouldReturnEmptyOptionalCaseSensitive() {
        Optional<Tag> actualTag = repository.findByName("test TAG");
        assertThat(actualTag).isEmpty();
    }

    @Test
    void checkExistByNameShouldReturnTrue() {
        boolean actualExist = repository.existsByName("Test tag");
        assertThat(actualExist).isTrue();
    }

    @Test
    void checkExistByNameShouldReturnFalseNotExist() {
        boolean actualExist = repository.existsByName("Test tag not exist");
        assertThat(actualExist).isFalse();
    }

    @Test
    void checkExistByNameShouldReturnFalseCaseSensitive() {
        boolean actualExist = repository.existsByName("test TAG");
        assertThat(actualExist).isFalse();
    }

    @Test
    void checkDeleteByIdShouldReturn1AffectedRowsTagExist() {
        int actualAffectedRows = repository.deleteById(1L);
        int expectedAffectedRows = 1;
        assertThat(actualAffectedRows).isEqualTo(expectedAffectedRows);
    }

    @Test
    void checkDeleteByIdShouldReturn0AffectedRowsTagNotExist() {
        int actualAffectedRows = repository.deleteById(10L);
        assertThat(actualAffectedRows).isZero();
    }

    @Test
    void checkFindMostWidelyUsedTagOfUserWithHighestOrdersCostShouldReturnTag() {
        Tag actualTag = repository.findMostWidelyUsedTagOfUserWithHighestOrdersCost();
        Tag expectedTag = getSimpleTag2();

        assertThat(actualTag).isEqualTo(expectedTag);
    }
}