package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.models.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static generators.factories.tags.TagDtoFactory.*;
import static generators.factories.tags.TagFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TagMapperTest {

    @Autowired
    private TagMapper mapper;

    @Test
    void checkTagToDtoShouldReturnCorrectDto() {
        Tag tag = getSimpleTag();
        TagDto actual = mapper.convertTagToDto(tag);
        TagDto expected = getSimpleTagDto();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkTagToDtoShouldReturnNull() {
        TagDto actual = mapper.convertTagToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkDtoToTagShouldReturnCorrectTag() {
        TagDto dto = getSimpleTagDto();
        Tag actual = mapper.convertDtoToTag(dto);
        Tag expected = getSimpleTagWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkDtoToTagShouldThrowExceptionIncorrectName() {
        TagDto dto = getTagDtoWithoutName();
        assertThatThrownBy(() -> mapper.convertDtoToTag(dto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkDtoToTagShouldReturnNull() {
        Tag actual = mapper.convertDtoToTag(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkTagsSetToDtosShouldReturnCorrectSet() {
        Set<Tag> tags = getDifferentTagsSet();
        Set<TagDto> actual = mapper.convertTagsSetToDto(tags);
        Set<TagDto> expected = getSimpleTagDtosSet();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkTagsSetToDtosShouldReturnNull() {
        Set<TagDto> actual = mapper.convertTagsSetToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkTagsListToDtosShouldReturnCorrectSet() {
        List<Tag> tags = getDifferentTagsList();
        List<TagDto> actual = mapper.convertTagsListToDtos(tags);
        List<TagDto> expected = getSimpleTagDtosList();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkTagsListToDtosShouldReturnNull() {
        List<TagDto> actual = mapper.convertTagsListToDtos(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkTagDtosToTagsShouldReturnCorrectTags() {
        List<TagDto> dtos = getSimpleTagDtosList();
        Set<Tag> actual = mapper.convertTagDtosToTags(dtos);
        Set<Tag> expected = getDifferentTagsWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkTagDtosToTagsShouldThrowExceptionBecauseContainInvalidDto() {
        List<TagDto> dtos = List.of(getSimpleTagDto(), getTagDtoWithoutName());
        assertThatThrownBy(() -> mapper.convertTagDtosToTags(dtos))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkTagDtosToTagsShouldReturnNull() {
        Set<Tag> actual = mapper.convertTagDtosToTags(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkUpdateTagShouldReturnTagWithUpdatedName() {
        Tag tag = getSimpleTag();
        TagDto dto = getSimpleTagDto2();
        Tag updatedActual = mapper.updateTag(dto, tag);
        Tag updatedExpected = new Tag(1L, "Test tag 2", Collections.emptySet());
        assertThat(updatedActual).isEqualTo(updatedExpected);
    }

    @Test
    void checkUpdateTagShouldThrowExceptionBecauseInvalidDto() {
        Tag tag = getSimpleTag();
        TagDto dto = getTagDtoWithoutName();
        assertThatThrownBy(() -> mapper.updateTag(dto, tag))
                .isInstanceOf(ConstraintViolationException.class);
    }
}