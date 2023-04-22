package ru.clevertec.ecl.services.impl;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dao.TagsRepository;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.responses.ModificationResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static generators.factories.PageFactory.*;
import static generators.factories.PageableFactory.*;
import static generators.factories.tags.TagDtoFactory.*;
import static generators.factories.tags.TagFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TagsServiceImplTest {

    @MockBean
    private TagsRepository repository;
    @Autowired
    private TagsServiceImpl service;

    @Test
    void checkGetAllTagsWithFilteringShouldReturnListOfDtos() {
        when(repository.findAll(any(Pageable.class))).thenReturn(getTagPage());

        List<TagDto> actual = service.getAllTags(getDefaultPageable());
        List<TagDto> expected = getSimpleTagDtosList();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void checkGetTagByIdShouldReturnTag() {
        when(repository.findById(1L)).thenReturn(Optional.of(getSimpleTag()));

        TagDto actual = service.getTagById(1);
        TagDto expected = getSimpleTagDto();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkGetTagByIdShouldThrowExceptionIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTagById(1))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Tag with ID 1 not found in database");
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void checkAddTagShouldReturnResponseWithGeneratedId() {
        when(repository.existsByName(anyString())).thenReturn(false);
        when(repository.save(any(Tag.class))).thenReturn(getSimpleTag());

        ModificationResponse actual = service.addTag(getSimpleTagDto());
        ModificationResponse expected = new ModificationResponse(1L, "Tag added successfully");

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).existsByName(anyString());
        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    void checkAddTagShouldThrowExceptionIncorrectDto() {
        assertThatThrownBy(() -> service.addTag(getTagDtoWithoutName()))
                .isInstanceOf(ConstraintViolationException.class);
        verify(repository, times(0)).existsByName(anyString());
        verify(repository, times(0)).save(any(Tag.class));
    }

    @Test
    void checkAddTagShouldThrowExceptionTagExist() {
        when(repository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> service.addTag(getSimpleTagDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: tag with name 'Test tag' already exist in database");
        verify(repository, times(1)).existsByName(anyString());
        verify(repository, times(0)).save(any(Tag.class));
    }

    @Test
    void checkAddAllTagsIfNotExistShouldAddOneValueThatNotPresent() {
        when(repository.findByName("Test tag")).thenReturn(Optional.of(getSimpleTag()));
        when(repository.findByName("Test tag 2")).thenReturn(Optional.of(getSimpleTag2()));
        when(repository.findByName("Test tag 3")).thenReturn(Optional.empty());
        when(repository.save(any(Tag.class))).thenReturn(getSimpleTag3());

        Set<Tag> actual = service.addAllTagsIfNotExist(getDifferentTagsSet());
        Set<Tag> expected = getDifferentTagsSet();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).save(any(Tag.class));
        verify(repository, times(3)).findByName(anyString());
    }

    @Test
    void checkUpdateTagShouldReturnResponseWithUpdatedId() {
        when(repository.existsByName(anyString())).thenReturn(false);
        when(repository.findById(1L)).thenReturn(Optional.of(getSimpleTag()));

        ModificationResponse actual = service.updateTag(1L, getSimpleTagDto());
        ModificationResponse expected = new ModificationResponse(1L, "Tag updated successfully");

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).existsByName(anyString());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any(Tag.class));
    }

    @Test
    void checkUpdateTagShouldThrowExceptionIncorrectDto() {
        when(repository.existsByName(any())).thenReturn(false);
        when(repository.findById(1L)).thenReturn(Optional.of(getSimpleTag()));

        assertThatThrownBy(() -> service.updateTag(1L, getTagDtoWithoutName()))
                .isInstanceOf(ConstraintViolationException.class);
        verify(repository, times(1)).existsByName(any());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Tag.class));
    }

    @Test
    void checkUpdateTagShouldThrowExceptionIdNotFound() {
        when(repository.existsByName(anyString())).thenReturn(false);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateTag(1L, getSimpleTagDto()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot update: tag with ID 1 not found in database");
        verify(repository, times(1)).existsByName(anyString());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(0)).save(any(Tag.class));
    }

    @Test
    void checkUpdateTagShouldThrowExceptionTagWithNameExist() {
        when(repository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> service.updateTag(1L, getSimpleTagDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: tag with name 'Test tag' already exist in database");
        verify(repository, times(1)).existsByName(anyString());
        verify(repository, times(0)).findById(anyLong());
        verify(repository, times(0)).save(any(Tag.class));
    }

    @Test
    void checkDeleteTagShouldReturnResponseWithDeletedId() {
        when(repository.deleteById(anyLong())).thenReturn(1);

        ModificationResponse actual = service.deleteTag(5L);
        ModificationResponse expected = new ModificationResponse(5L, "Tag deleted successfully");

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void checkDeleteTagShouldThrowExceptionIdNotFound() {
        when(repository.deleteById(anyLong())).thenReturn(0);

        assertThatThrownBy(() -> service.deleteTag(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: tag with ID 1 not found in database");
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void checkGetMostWidelyUsedTagOfUserWithHighestOrdersCostShouldReturnTag() {
        when(repository.findMostWidelyUsedTagOfUserWithHighestOrdersCost()).thenReturn(getSimpleTag());

        TagDto actual = service.getMostWidelyUsedTagOfUserWithHighestOrdersCost();
        TagDto expected = getSimpleTagDto();

        assertThat(actual).isEqualTo(expected);
        verify(repository, times(1)).findMostWidelyUsedTagOfUserWithHighestOrdersCost();
    }
}