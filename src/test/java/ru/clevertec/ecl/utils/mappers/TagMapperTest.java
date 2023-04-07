package ru.clevertec.ecl.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.exceptions.InvalidItemException;
import ru.clevertec.ecl.models.Tag;

import java.util.Collections;
import java.util.List;

import static generators.factories.TagDTOFactory.*;
import static generators.factories.TagFactory.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class TagMapperTest {

    private TagMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TagMapper.class);
    }

    @Test
    void checkTagToDTOShouldReturnCorrectDTO() {
        Tag tag = getSimpleTag();
        TagDto actual = mapper.tagToDTO(tag);
        TagDto expected = getSimpleTagDTO();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkTagToDTOShouldReturnNull() {
        TagDto actual = mapper.tagToDTO(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkDTOtoTagShouldReturnCorrectTag() {
        TagDto dto = getSimpleTagDTO();
        Tag actual = mapper.dtoToTag(dto);
        Tag expected = getSimpleTagWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkDTOtoTagShouldThrowExceptionIncorrectName() {
        TagDto dto = getTagDTOWithoutName();
        assertThatThrownBy(() -> mapper.dtoToTag(dto))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void checkDTOtoTagShouldReturnNull() {
        Tag actual = mapper.dtoToTag(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkAllTagsToDTOShouldReturnCorrectTagsDTO() {
        List<Tag> dtoList = getDifferentTags();
        List<TagDto> actual = mapper.tagsToDto(dtoList);
        List<TagDto> expected = getSimpleTagDTOs();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllTagsToDTOShouldReturnNull() {
        List<TagDto> actual = mapper.tagsToDto(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkAllDTOToTagsShouldReturnCorrectTags() {
        List<TagDto> dtoList = getSimpleTagDTOs();
        List<Tag> actual = mapper.DtosToTag(dtoList);
        List<Tag> expected = getDifferentTagsWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllDTOToTagsShouldThrowExceptionBecauseContainInvalidValue() {
        List<TagDto> dtoList = List.of(getSimpleTagDTO(), getTagDTOWithoutName());
        assertThatThrownBy(() -> mapper.DtosToTag(dtoList))
                .isInstanceOf(InvalidItemException.class);
    }

    @Test
    void checkAllDTOToTagsShouldReturnNull() {
        List<Tag> actual = mapper.DtosToTag(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkValidateTagShouldBeValid() {
        Tag tag = getSimpleTag();
        assertThatCode(() -> mapper.validateTag(tag)).doesNotThrowAnyException();
    }

    @Test
    void checkValidateTagShouldThrowExceptionNullName() {
        Tag tag = new Tag();
        assertThatThrownBy(() -> mapper.validateTag(tag)).isInstanceOf(InvalidItemException.class);
    }

    @Test
    void checkValidateTagShouldThrowExceptionBlankName() {
        Tag tag = new Tag(1, "", Collections.emptySet());
        assertThatThrownBy(() -> mapper.validateTag(tag)).isInstanceOf(InvalidItemException.class);
    }
}