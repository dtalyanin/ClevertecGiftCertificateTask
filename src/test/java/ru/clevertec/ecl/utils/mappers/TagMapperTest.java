package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.models.Tag;

import java.util.List;

import static generators.factories.TagDTOFactory.*;
import static generators.factories.TagFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class TagMapperTest {
    @Autowired
    private TagMapper mapper;

    @Test
    void checkTagToDTOShouldReturnCorrectDTO() {
        Tag tag = getSimpleTag();
        TagDTO actual = mapper.tagToDTO(tag);
        TagDTO expected = getSimpleTagDTO();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkTagToDTOShouldReturnNull() {
        TagDTO actual = mapper.tagToDTO(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkDTOtoTagShouldReturnCorrectTag() {
        TagDTO dto = getSimpleTagDTO();
        Tag actual = mapper.dtoToTag(dto);
        Tag expected = getSimpleTagWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkDTOtoTagShouldThrowExceptionIncorrectName() {
        TagDTO dto = getTagDTOWithoutName();
        assertThatThrownBy(() -> mapper.dtoToTag(dto))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkDTOtoTagShouldReturnNull() {
        Tag actual = mapper.dtoToTag(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkAllTagsToDTOShouldReturnCorrectTagsDTO() {
        List<Tag> dtoList = getDifferentTags();
        List<TagDTO> actual = mapper.allTagsToDTO(dtoList);
        List<TagDTO> expected = getSimpleTagDTOs();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllTagsToDTOShouldReturnNull() {
        List<TagDTO> actual = mapper.allTagsToDTO(null);
        assertThat(actual).isNull();
    }

    @Test
    void checkAllDTOToTagsShouldReturnCorrectTags() {
        List<TagDTO> dtoList = getSimpleTagDTOs();
        List<Tag> actual = mapper.allDTOToTags(dtoList);
        List<Tag> expected = getDifferentTagsWithoutId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkAllDTOToTagsShouldThrowExceptionBecauseContainInvalidValue() {
        List<TagDTO> dtoList = List.of(getSimpleTagDTO(), getTagDTOWithoutName());
        assertThatThrownBy(() -> mapper.allDTOToTags(dtoList))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAllDTOToTagsShouldReturnNull() {
        List<Tag> actual = mapper.allDTOToTags(null);
        assertThat(actual).isNull();
    }
}