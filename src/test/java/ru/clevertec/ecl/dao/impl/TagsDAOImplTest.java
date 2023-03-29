package ru.clevertec.ecl.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.configuration.TestConfig;
import ru.clevertec.ecl.dao.TagsDAO;
import ru.clevertec.ecl.models.Tag;

import java.util.List;
import java.util.Optional;

import static generators.factories.TagFactory.getDifferentTags;
import static generators.factories.TagFactory.getSimpleTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
@Rollback
class TagsDAOImplTest {

    @Autowired
    private TagsDAO dao;

    @Test
    void checkGetAllTagsShouldReturn3Tags() {
        List<Tag> actual = dao.getAllTags();
        List<Tag> expected = getDifferentTags();
        assertThat(actual)
                .isEqualTo(expected)
                .hasSize(expected.size());
    }

    @Test
    void checkGetTagByIdShouldReturnTag() {
        Optional<Tag> actual = dao.getTagById(1);
        Optional<Tag> expected = Optional.of(getSimpleTag());
        assertThat(actual)
                .isPresent()
                .isEqualTo(expected);
    }

    @Test
    void checkGetTagByIdShouldReturnEmptyOptional() {
        Optional<Tag> actual = dao.getTagById(4);
        assertThat(actual)
                .isEmpty();
    }

    @Test
    void checkGetTagByNameShouldReturnTag() {
        Optional<Tag> actual = dao.getTagByName("Test tag");
        Optional<Tag> expected = Optional.of(getSimpleTag());
        assertThat(actual)
                .isPresent()
                .isEqualTo(expected);
    }

    @Test
    void checkGetTagByNameShouldReturnEmptyOptional() {
        Optional<Tag> actual = dao.getTagByName("Test");
        assertThat(actual)
                .isEmpty();
    }

    @Test
    void checkAddTagShouldReturnGeneratedId4() {
        long actual = dao.addTag(new Tag(0, "Test"));
        assertThat(actual).isNotZero();
    }

    @Test
    void checkAddTagShouldThrowExceptionTagWithNameExist() {
        assertThatThrownBy(() -> dao.addTag(getSimpleTag()))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void checkUpdateTagShouldReturnUpdatedRowsCount1() {
        Tag expectedTag = new Tag(1, "Test tag updated");
        int actualUpdatedRows = dao.updateTag(1, expectedTag);
        Tag actualTag = dao.getTagById(1L).get();
        int expectedUpdatedRows = 1;
        assertThat(actualUpdatedRows).isEqualTo(expectedUpdatedRows);
        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void checkUpdateTagShouldReturnNoUpdatedRows() {
        Tag expectedTag = new Tag(1, "Test tag updated");
        int actualUpdatedRows = dao.updateTag(4L, expectedTag);
        int expectedUpdatedRows = 0;
        assertThat(actualUpdatedRows).isEqualTo(expectedUpdatedRows);
    }

    @Test
    void checkUpdateTagShouldThrowExceptionTagWithNameExist() {
        Tag updatedValue = getSimpleTag();
        assertThatThrownBy(() -> dao.updateTag(2L, updatedValue))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void checkDeleteTagReturnDeletedRowsCount1() {
        int actualDeletedRows = dao.deleteTag(1L);
        int expectedDeletedRows = 1;
        Optional<Tag> tagAfterDeleting = dao.getTagById(1L);
        assertThat(actualDeletedRows).isEqualTo(expectedDeletedRows);
        assertThat(tagAfterDeleting).isEmpty();
    }

    @Test
    void checkDeleteTagReturnNoDeletedRows() {
        int actualDeletedRows = dao.deleteTag(4L);
        int expectedDeletedRows = 0;
        assertThat(actualDeletedRows).isEqualTo(expectedDeletedRows);
    }
}