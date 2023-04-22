package ru.clevertec.ecl.integration.services;

import generators.factories.tags.TagFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.dao.TagsRepository;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.impl.TagsServiceImpl;

import java.util.List;

import static generators.factories.PageableFactory.*;
import static generators.factories.responses.ModificationResponseFactory.*;
import static generators.factories.tags.TagDtoFactory.*;
import static generators.factories.tags.TagFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagsServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TagsServiceImpl service;
    @Autowired
    private TagsRepository repository;

    @Test
    void checkGetAllTagsShouldReturn3TagDtosWithDefaultPagination() {
        List<TagDto> actualTagDtos = service.getAllTags(getDefaultPageable());
        List<TagDto> expectedTagDtos = getSimpleTagDtosList();

        assertThat(actualTagDtos)
                .containsAll(expectedTagDtos)
                .hasSameSizeAs(expectedTagDtos);
    }

    @Test
    void checkGetAllTagsShouldReturn2TagDtosWithPageSize2() {
        List<TagDto> actualTagDtos = service.getAllTags(getPageableWithSize2());
        List<TagDto> expectedTagDtos = getTagDtosWithSize2();

        assertThat(actualTagDtos)
                .containsAll(expectedTagDtos)
                .hasSameSizeAs(expectedTagDtos);
    }

    @Test
    void checkGetAllTagsShouldReturn3TagDtosWithFirstPage() {
        List<TagDto> actualTagDtos = service.getAllTags(getPageableWithFirstPage());
        List<TagDto> expectedTagDtos = getSimpleTagDtosList();

        assertThat(actualTagDtos)
                .containsAll(expectedTagDtos)
                .hasSameSizeAs(expectedTagDtos);
    }

    @Test
    void checkGetAllTagsShouldReturnTagDtosEmptyListOutOfPageRange() {
        List<TagDto> expectedTagDtos = service.getAllTags(getPageableWithOutOfPageRange());
        assertThat(expectedTagDtos).isEmpty();
    }

    @Test
    void checkGetAllTagsShouldReturnTag2DtosWithPageSize2AngIncludeFirstPage() {
        List<TagDto> actualTagDtos = service.getAllTags(getPageableWithFirstPageAndSize2());
        List<TagDto> expectedTagDtos = getTagDtosWithSize2();

        assertThat(actualTagDtos)
                .containsAll(expectedTagDtos)
                .hasSameSizeAs(expectedTagDtos);
    }

    @Test
    void checkGetTagByIdShouldReturnTagDtoWithSpecifiedId() {
        TagDto actualTagDto = service.getTagById(1L);
        TagDto expectedTagDto = getSimpleTagDto();

        assertThat(actualTagDto).isEqualTo(expectedTagDto);
    }

    @Test
    void checkGetTagByIdShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.getTagById(10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Tag with ID 10 not found in database");
    }

    @Test
    void checkAddTagShouldReturnResponseWithGeneratedId() {
        ModificationResponse actualModificationResponse = service.addTag(getSimpleTagDtoToCreate());
        ModificationResponse expectedModificationResponse = getTagAddedResponse();

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
    }

    @Test
    void checkAddTagShouldExistInDbAfterExecuting() {
        service.addTag(getSimpleTagDtoToCreate());
        Tag actualTag = repository.findById(4L).get();
        Tag expectedTag = getCreatedTag();

        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void checkAddTagShouldThrowExceptionEmptyTagName() {
        assertThatThrownBy(() -> service.addTag(getTagDtoWithoutName()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkAddTagShouldThrowExceptionTagExist() {
        assertThatThrownBy(() -> service.addTag(getSimpleTagDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot add: tag with name 'Test tag' already exist in database");
    }

    @Test
    void checkAddAllTagsIfNotExistShouldAddOneNewTag() {
        service.addAllTagsIfNotExist(getDifferentTagsSetWithOneNew());
        List<Tag> actualTags = repository.findAll();
        List<Tag> expectedTags = TagFactory.getDifferentTagsListWithCreatedTag();

        assertThat(actualTags)
                .containsAll(expectedTags)
                .hasSameSizeAs(expectedTags);
    }

    @Test
    void checkAddAllTagsIfNotExistShouldNotAddAllAlreadyExist() {
        List<Tag> beforeExecution = repository.findAll();
        service.addAllTagsIfNotExist(getDifferentTagsSet());
        List<Tag> afterExecution = repository.findAll();

        assertThat(beforeExecution).isEqualTo(afterExecution);
    }

    @Test
    void checkUpdateTagShouldReturnResponseWithUpdatedId() {
        ModificationResponse actualModificationResponse = service.updateTag(1L, getSimpleTagDtoToUpdate());
        ModificationResponse expectedModificationResponse = getTagUpdatedResponse();

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
    }

    @Test
    void checkUpdateTagShouldExistWithNewName() {
        service.updateTag(1L, getSimpleTagDtoToUpdate());
        Tag actualTag = repository.findById(1L).get();
        Tag expectedTag = getUpdatedTag();

        assertThat(actualTag).isEqualTo(expectedTag);
    }

    @Test
    void checkUpdateTagShouldThrowExceptionIncorrectDto() {
        assertThatThrownBy(() -> service.updateTag(1L, getTagDtoWithoutName()))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void checkUpdateTagShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.updateTag(10L, getSimpleTagDtoToUpdate()))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot update: tag with ID 10 not found in database");
    }

    @Test
    void checkUpdateTagShouldThrowExceptionTagWithNameExist() {
        assertThatThrownBy(() -> service.updateTag(3L, getSimpleTagDto()))
                .isInstanceOf(ItemExistException.class)
                .hasMessage("Cannot update: tag with name 'Test tag' already exist in database");
    }

    @Test
    void checkDeleteTagShouldReturnResponseWithDeletedId() {
        ModificationResponse actualModificationResponse = service.deleteTag(3L);
        ModificationResponse expectedModificationResponse = getTagDeletedResponse();

        assertThat(actualModificationResponse).isEqualTo(expectedModificationResponse);
    }

    @Test
    void checkDeleteTagShouldNotExistInDbAfterExecuting() {
        service.deleteTag(3L);
        boolean existAfterExecuting = repository.existsById(3L);

        assertThat(existAfterExecuting).isFalse();
    }

    @Test
    void checkDeleteTagShouldThrowExceptionIdNotFound() {
        assertThatThrownBy(() -> service.deleteTag(10L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Cannot delete: tag with ID 10 not found in database");
    }

    @Test
    void checkGetMostWidelyUsedTagOfUserWithHighestOrdersCostShouldReturnTag() {
        TagDto actualTagDto = service.getMostWidelyUsedTagOfUserWithHighestOrdersCost();
        TagDto expectedTagDto = getSimpleTagDto2();

        assertThat(actualTagDto).isEqualTo(expectedTagDto);
    }
}
