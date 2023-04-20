package ru.clevertec.ecl.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import generators.factories.tags.TagDtoFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.dao.TagsRepository;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static generators.factories.tags.TagDtoFactory.*;
import static generators.factories.tags.TagFactory.getCreatedTag;
import static generators.factories.tags.TagFactory.getUpdatedTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TagsControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TagsRepository repository;


    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithoutPagination() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithPageSize2() {
        List<TagDto> tagDtos = List.of(getSimpleTagDto(), getSimpleTagDto2());
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithFirstPage() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosEmptyListOutOfPageRange() {
        List<TagDto> tagDtos = Collections.emptyList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithPageSize2AngIncludeFirstPage() {
        List<TagDto> tagDtos = List.of(getSimpleTagDto(), getSimpleTagDto2());
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("size", "2")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithDefaultPaginationWhenNegativePageSize() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithDefaultPaginationWhenNegativePage() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnTagDtoWithSpecifiedId() {
        TagDto dto = TagDtoFactory.getSimpleTagDto();
        String json = mapper.writeValueAsString(dto);

        mvc.perform(get("/tags/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnErrorResponseWithIncorrectId() {
        ErrorResponse response = new ErrorResponse("Min ID value is 1", ErrorCode.INVALID_FIELD_VALUE.getCode());
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(get("/tags/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));

    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = new ErrorResponse("Tag with ID 10 not found in database",
                ErrorCode.TAG_ID_NOT_FOUND.getCode());
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(get("/tags/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));

    }

    @Test
    @SneakyThrows
    void checkAddTagShouldReturnResponseWithGeneratedId() {
        TagDto dtoToCreate = getSimpleTagDtoToCreate();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToCreate);
        ModificationResponse modificationResponse = new ModificationResponse(4L, "Tag added successfully");
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonModificationResponse))
                .andExpect(header().string("Location", containsString("tags/4")));
    }

    @Test
    @SneakyThrows
    void checkAddTagShouldExistInDbAfterExecuting() {
        TagDto dtoToCreate = getSimpleTagDtoToCreate();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToCreate);

        mvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTagToCreate));
        System.out.println(repository.findAll());
        Tag expectedTag = getCreatedTag();
        Optional<Tag> actualTag = repository.findById(4L);

        assertThat(actualTag).hasValue(expectedTag);


    }

    @Test
    @SneakyThrows
    void checkAddTagShouldReturnErrorResponseDtoWithEmptyTagName() {
        TagDto dtoToCreate = getTagDtoWithoutName();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse(null,
                "Tag name must contain at least 1 character", ErrorCode.INVALID_TAG_FIELD_VALUE.getCode());
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddTagShouldReturnErrorResponseTagWithNameExist() {
        TagDto dtoToCreate = getSimpleTagDto();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToCreate);
        ErrorResponse errorResponse = new ErrorResponse("Cannot add: tag with name 'Test tag' already exist in database",
                ErrorCode.TAG_NAME_EXIST.getCode());
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateTagShouldReturnResponseWithUpdatedId() {
        ModificationResponse modificationResponse = new ModificationResponse(1L, "Tag updated successfully");
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);
        TagDto dtoToUpdate = getSimpleTagDtoToUpdate();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToUpdate);

        mvc.perform(put("/tags/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonModificationResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateTagShouldExistWithUpdatedName() {
        TagDto dtoToUpdate = getSimpleTagDtoToUpdate();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToUpdate);

        mvc.perform(put("/tags/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate));

        Tag expectedTag = getUpdatedTag();
        Optional<Tag> actualTag = repository.findById(1L);

        assertThat(actualTag).hasValue(expectedTag);
    }

    @Test
    void deleteTagById() {
    }

    @Test
    void getMostWidelyUsedTagOfUserWithHighestOrdersCost() {
    }
}