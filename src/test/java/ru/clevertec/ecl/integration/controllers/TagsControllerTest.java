package ru.clevertec.ecl.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;

import java.util.List;

import static generators.factories.responses.ErrorResponseFactory.*;
import static generators.factories.responses.ModificationResponseFactory.*;
import static generators.factories.responses.ValidationErrorResponseFactory.getTagEmptyNameResponse;
import static generators.factories.tags.TagDtoFactory.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TagsControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturn3TagDtosWithDefaultPagination() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturn2TagDtosWithPageSize2() {
        List<TagDto> tagDtos = getTagDtosWithSize2();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturn3TagDtosWithFirstPage() {
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
        List<TagDto> tagDtos = getEmptyListTagDtos();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTag2DtosWithPageSize2AngIncludeFirstPage() {
        List<TagDto> tagDtos = getTagDtosWithSize2();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("size", "2")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturn3TagDtosWithDefaultPaginationWhenNegativeSize() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturn3TagDtosWithDefaultPaginationWhenNegativePage() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllTagsShouldReturnTagDtosWithoutSortingIfSortParamExist() {
        List<TagDto> tagDtos = getSimpleTagDtosList();
        String jsonTagDtos = mapper.writeValueAsString(tagDtos);

        mvc.perform(get("/tags")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnTagDtoWithSpecifiedId() {
        TagDto tagDto = getSimpleTagDto();
        String jsonTagDto = mapper.writeValueAsString(tagDto);

        mvc.perform(get("/tags/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDto));
    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnErrorResponseWithIncorrectId() {
        ErrorResponse response = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(get("/tags/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = getTagIdNotFoundResponse();
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
        ModificationResponse modificationResponse = getTagAddedResponse();
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
    void checkAddTagShouldReturnErrorResponseWithEmptyTagName() {
        TagDto dtoToCreate = getTagDtoWithoutName();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getTagEmptyNameResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddTagShouldReturnErrorResponseWithNameExist() {
        TagDto dtoToCreate = getSimpleTagDto();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToCreate);
        ErrorResponse errorResponse = getTagCannotAddExistResponse();
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
        TagDto dtoToUpdate = getSimpleTagDtoToUpdate();
        String jsonTagToCreate = mapper.writeValueAsString(dtoToUpdate);
        ModificationResponse modificationResponse = getTagUpdatedResponse();
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(put("/tags/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToCreate))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonModificationResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateTagByIdShouldReturnErrorResponseWithIncorrectId() {
        TagDto dtoToUpdate = getSimpleTagDtoToUpdate();
        String jsonTagToUpdate = mapper.writeValueAsString(dtoToUpdate);
        ErrorResponse errorResponse = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(put("/tags/{id}", -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateTagByIdShouldReturnErrorResponseWithIdNotFound() {
        TagDto dtoToUpdate = getSimpleTagDtoToUpdate();
        String jsonTagToUpdate = mapper.writeValueAsString(dtoToUpdate);
        ErrorResponse errorResponse = getTagCannotUpdateIdNotFound();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(put("/tags/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToUpdate))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateTagByIdShouldReturnErrorResponseWithNameExist() {
        TagDto dtoToUpdate = getSimpleTagDto2();
        String jsonTagToUpdate = mapper.writeValueAsString(dtoToUpdate);
        ErrorResponse errorResponse = getTagCannotUpdateExistResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(put("/tags/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToUpdate))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateTagByIdShouldReturnErrorResponseWithEmptyTagName() {
        TagDto dtoToCreate = getTagDtoWithoutName();
        String jsonTagToUpdate = mapper.writeValueAsString(dtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getTagEmptyNameResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(put("/tags/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTagToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteTagShouldReturnResponseWithDeletedId() {
        ModificationResponse modificationResponse = getTagDeletedResponse();
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(delete("/tags/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonModificationResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteTagByIdShouldReturnErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = getTagCannotDeleteIdNotFound();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(delete("/tags/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteTagByIdShouldReturnErrorResponseWithIncorrectId() {
        ErrorResponse errorResponse = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(delete("/tags/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    void getMostWidelyUsedTagOfUserWithHighestOrdersCost() {
    }
}