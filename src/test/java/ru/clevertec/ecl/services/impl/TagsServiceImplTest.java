package ru.clevertec.ecl.services.impl;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import ru.clevertec.ecl.dao.TagsDAO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.responses.ModificationResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static generators.factories.TagDTOFactory.*;
import static generators.factories.TagFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagsServiceImplTest {
    @Mock
    private TagsDAO dao;
    @InjectMocks
    private TagsServiceImpl service;

    @Test
    void checkGetAllTagsShouldReturnListOfDTOs() {
        when(dao.getAllTags()).thenReturn(getDifferentTags());
        List<TagDTO> actual = service.getAllTags();
        List<TagDTO> expected = getSimpleTagDTOs();
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).getAllTags();
    }

    @Test
    void checkGetAllTagsShouldReturnEmptyListIfNoTags() {
        when(dao.getAllTags()).thenReturn(Collections.emptyList());
        List<TagDTO> actual = service.getAllTags();
        List<TagDTO> expected = Collections.emptyList();
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).getAllTags();
    }

    @Test
    void checkGetTagByIdShouldReturnTag() {
        when(dao.getTagById(1)).thenReturn(Optional.of(getSimpleTag()));
        TagDTO actual = service.getTagById(1);
        TagDTO expected = getSimpleTagDTO();
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).getTagById(anyLong());
    }

    @Test
    void checkGetTagByIdShouldThrowExceptionIdNotFound() {
        when(dao.getTagById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getTagById(1))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Tag with ID 1 not found in database");
        verify(dao, times(1)).getTagById(anyLong());
    }

    @Test
    void checkAddTagShouldReturnResponseWithGeneratedId() {
        when(dao.addTag(any(Tag.class))).thenReturn(1L);
        ModificationResponse actual = service.addTag(getSimpleTagDTO());
        ModificationResponse expected = new ModificationResponse(1L, "Tag added successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).addTag(any(Tag.class));
    }

    @Test
    void checkAddTagShouldThrowExceptionIncorrectDTO() {
        assertThatThrownBy(() -> service.addTag(getTagDTOWithoutName()))
                .isInstanceOf(ConstraintViolationException.class);
        verify(dao, times(0)).addTag(any(Tag.class));
    }

    @Test
    void checkAddTagShouldThrowExceptionTagExist() {
        when(dao.addTag(any(Tag.class))).thenThrow(new DuplicateKeyException("Message"));
        assertThatThrownBy(() -> service.addTag(getSimpleTagDTO()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: tag with name 'Test tag' already exist in database");
        verify(dao, times(1)).addTag(any(Tag.class));
    }

    @Test
    void checkAddAllTagsIfNotExistShouldAddOneValueThatNotPresent() {
        List<Tag> tags = getDifferentTags();
        when(dao.getTagByName("Test tag")).thenReturn(Optional.of(tags.get(0)));
        when(dao.getTagByName("Test tag 2")).thenReturn(Optional.of(tags.get(1)));
        when(dao.getTagByName("Test tag 3")).thenReturn(Optional.empty());
        when(dao.addTag(tags.get(2))).thenReturn(3L);
        List<Long> actual = service.addAllTagsIfNotExist(tags);
        List<Long> expected = List.of(1L, 2L , 3L);
        assertThat(actual)
                .containsAll(expected)
                .hasSize(expected.size());
        verify(dao, times(1)).addTag(any(Tag.class));
        verify(dao, times(3)).getTagByName(anyString());
    }

    @Test
    void checkAddAllTagsIfNotExistShouldAddOneValueBecauseTwoTagsAreEquals() {
        List<Tag> tags = getSimpleTags();
        when(dao.getTagByName(anyString())).thenReturn(Optional.empty());
        when(dao.addTag(any(Tag.class))).thenReturn(0L);
        List<Long> actual = service.addAllTagsIfNotExist(tags);
        List<Long> expected = Collections.singletonList(0L);
        assertThat(actual)
                .containsAll(expected)
                .hasSize(expected.size());
        verify(dao, times(1)).addTag(any(Tag.class));
        verify(dao, times(1)).getTagByName(anyString());
    }

    @Test
    void checkUpdateTagShouldReturnResponseWithUpdatedId() {
        when(dao.updateTag(anyLong(), any(Tag.class))).thenReturn(1);
        ModificationResponse actual = service.updateTag(5L, getSimpleTagDTO());
        ModificationResponse expected = new ModificationResponse(5L, "Tag updated successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).updateTag(anyLong(), any(Tag.class));
    }

    @Test
    void checkUpdateTagShouldThrowExceptionIncorrectDTO() {
        assertThatThrownBy(() -> service.updateTag(1L, getTagDTOWithoutName()))
                .isInstanceOf(ConstraintViolationException.class);
        verify(dao, times(0)).updateTag(anyLong(), any(Tag.class));
    }

    @Test
    void checkUpdateTagShouldThrowExceptionIdNotFound() {
        when(dao.updateTag(anyLong(), any(Tag.class))).thenReturn(0);
        assertThatThrownBy(() -> service.updateTag(1L, getSimpleTagDTO()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot update: tag with ID 1 not found");
        verify(dao, times(1)).updateTag(anyLong(), any(Tag.class));
    }

    @Test
    void checkUpdateTagShouldThrowExceptionTagExist() {
        when(dao.updateTag(anyLong(), any(Tag.class))).thenThrow(new DuplicateKeyException("Message"));
        assertThatThrownBy(() -> service.updateTag(1L, getSimpleTagDTO()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: tag with name 'Test tag' already exist in database");
        verify(dao, times(1)).updateTag(anyLong(), any(Tag.class));
    }

    @Test
    void checkDeleteTagShouldReturnResponseWithDeletedId() {
        when(dao.deleteTag(anyLong())).thenReturn(1);
        ModificationResponse actual = service.deleteTag(5L);
        ModificationResponse expected = new ModificationResponse(5L, "Tag deleted successfully");
        assertThat(actual).isEqualTo(expected);
        verify(dao, times(1)).deleteTag(anyLong());
    }

    @Test
    void checkDeleteTagShouldThrowExceptionIdNotFound() {
        when(dao.deleteTag(anyLong())).thenReturn(0);
        assertThatThrownBy(() -> service.deleteTag(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: tag with ID 1 not found");
        verify(dao, times(1)).deleteTag(anyLong());
    }
}