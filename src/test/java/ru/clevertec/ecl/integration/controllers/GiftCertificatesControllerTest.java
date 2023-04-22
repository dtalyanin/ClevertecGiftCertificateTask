package ru.clevertec.ecl.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.dto.certificates.GiftCertificateDto;
import ru.clevertec.ecl.dto.certificates.UpdateGiftCertificateDto;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;
import ru.clevertec.ecl.models.responses.errors.MultipleFieldsValidationErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;

import java.util.List;

import static generators.factories.certificates.GiftCertificateDtoFactory.*;
import static generators.factories.certificates.UpdateGiftCertificateDtoFactory.*;
import static generators.factories.responses.ErrorResponseFactory.*;
import static generators.factories.responses.ModificationResponseFactory.*;
import static generators.factories.responses.ValidationErrorResponseFactory.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class GiftCertificatesControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    void getAllGiftCertificatesWithFilteringShouldReturn3CertificateDtosWithDefaultPagination() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonCertificateDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonCertificateDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn2DtosWithPageSize2() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbWithSize2();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtosWithFirstPage() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosEmptyListOutOfPageRange() {
        List<GiftCertificateDto> certificateDtos = getEmptyListGiftCertificateDtos();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn2DtosWithPageSize2AngIncludeFirstPage() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbWithSize2();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("size", "2")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtosWithDefaultPaginationWhenNegativeSize() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtosWithDefaultPaginationWhenNegativePage() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByName() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbSortByNameOrCrateDateAsc();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos, true));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByNameDesc() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbSortByNameDesc();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("sort", "name,desc"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos, true));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByCreateDate() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbSortByNameOrCrateDateAsc();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("sort", "createDate"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos, true));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByCreateDateDesc() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbSortByCreateDateDesc();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("sort", "createDate,desc"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos, true));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnDtosWithSortByCreateDateDescAndNameDesc() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbSortByCreateDateDescAndNameDesc();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("sort", "createDate,desc")
                        .param("sort", "name,desc"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos, true));
    }


    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldIgnoreUnknownOrders() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbSortByNameDesc();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("sort", "name,desc")
                        .param("sort", "price"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos, true));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn1DtoWithFilterByFullName() {
        List<GiftCertificateDto> certificateDtos = getOneGiftCertificateDtosAfterFiltering();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("name", "Test 2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfName() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfNameIgnoreCase() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("name", "tEsT"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn1DtoWithFilterByFullDescription() {
        List<GiftCertificateDto> certificateDtos = getOneGiftCertificateDtosAfterFiltering();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("description", "Test description 2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfDescription() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("description", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn3DtoWithFilterByPartOfDescriptionIgnoreCase() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("description", "tEsT"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn2DtoWithFilterByOneTag() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDbWithSize2();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("tags", "Test tag"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturn1DtoWithFilterByTwoTags() {
        List<GiftCertificateDto> certificateDtos = getOneGiftCertificateDtosAfterFilteringByTags();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("tags", "Test tag")
                        .param("tags", "Test tag 3"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificatesWithFilteringShouldReturnEmptyListUnknownTag() {
        List<GiftCertificateDto> certificateDtos = getEmptyListGiftCertificateDtos();
        String jsonTagDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates")
                        .param("tags", "Test tag 10"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonTagDtos));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificateByIdShouldReturnCertificateDtoWithSpecifiedId() {
        GiftCertificateDto certificateDto = getSimpleGiftCertificateDtoWithTags();
        String jsonCertificateDto = mapper.writeValueAsString(certificateDto);

        mvc.perform(get("/certificates/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonCertificateDto));
    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnErrorResponseWithIncorrectId() {
        ErrorResponse response = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(get("/certificates/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificateByIdShouldErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = getCertificateIdNotFoundResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(get("/certificates/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnResponseWithGeneratedId() {
        GiftCertificateDto certificateDtoToCreate = getSimpleGiftCertificateDtoToCreate();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        ModificationResponse modificationResponse = getCertificateAddedResponse();
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonModificationResponse))
                .andExpect(header().string("Location", containsString("certificates/4")));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnErrorResponseWithEmptyCertificateName() {
        GiftCertificateDto certificateDtoToCreate = getGiftCertificateDtoWithEmptyName();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getCertificateEmptyNameResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnErrorResponseWithEmptyCertificateDescription() {
        GiftCertificateDto certificateDtoToCreate = getGiftCertificateDtoWithEmptyDescription();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getCertificateEmptyDescriptionResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnErrorResponseWithZeroPrice() {
        GiftCertificateDto certificateDtoToCreate = getGiftCertificateDtoWithZeroPrice();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getCertificateZeroPriceResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnErrorResponseWithZeroDuration() {
        GiftCertificateDto certificateDtoToCreate = getGiftCertificateDtoWithZeroDuration();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getCertificateZeroDurationResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnErrorResponseWithNullNameDescriptionPriceDuration() {
        GiftCertificateDto certificateDtoToCreate = getGiftCertificateDtoWithoutFields();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        MultipleFieldsValidationErrorResponse errorResponse = getCertificateNullNameDescriptionPriceDurationResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddGiftCertificateShouldReturnErrorResponseWithCertificateExist() {
        GiftCertificateDto certificateDtoToCreate = getSimpleGiftCertificateDto();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);

        ErrorResponse errorResponse = getCertificateCannotAddExistResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnResponseWithUpdatedId() {
        UpdateGiftCertificateDto certificateDtoToUpdate = getSimpleUpdateGiftCertificateDto();
        String jsonCertificateToUpdate = mapper.writeValueAsString(certificateDtoToUpdate);
        ModificationResponse modificationResponse = getCertificateUpdatedResponse();
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(patch("/certificates/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToUpdate))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonModificationResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithIdNotFound() {
        UpdateGiftCertificateDto certificateDtoToUpdate = getSimpleUpdateGiftCertificateDto();
        String jsonCertificateToUpdate = mapper.writeValueAsString(certificateDtoToUpdate);
        ErrorResponse errorResponse = getCertificateCannotUpdateIdNotFound();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(patch("/certificates/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToUpdate))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithIncorrectId() {
        UpdateGiftCertificateDto certificateDtoToUpdate = getSimpleUpdateGiftCertificateDto();
        String jsonCertificateToUpdate = mapper.writeValueAsString(certificateDtoToUpdate);
        ErrorResponse response = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(patch("/certificates/{id}", -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithNoFieldsToUpdate() {
        UpdateGiftCertificateDto certificateDtoToUpdate = getUpdateGiftCertificateDtoWithoutFields();
        String jsonCertificateToUpdate = mapper.writeValueAsString(certificateDtoToUpdate);
        ErrorResponse response = getCertificateCannotUpdateNoFieldsToUpdate();
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(patch("/certificates/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithEmptyCertificateName() {
        UpdateGiftCertificateDto updateCertificateDto = getUpdateGiftCertificateDtoWithEmptyName();
        String jsonCertificateToCreate = mapper.writeValueAsString(updateCertificateDto);
        SingleFieldValidationErrorResponse errorResponse = getCertificateEmptyNameResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithEmptyCertificateDescription() {
        UpdateGiftCertificateDto updateCertificateDto = getUpdateGiftCertificateDtoWithEmptyDescription();
        String jsonCertificateToCreate = mapper.writeValueAsString(updateCertificateDto);
        SingleFieldValidationErrorResponse errorResponse = getCertificateEmptyDescriptionResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithZeroPrice() {
        UpdateGiftCertificateDto updateCertificateDto = getUpdateGiftCertificateDtoWithZeroPrice();
        String jsonCertificateToCreate = mapper.writeValueAsString(updateCertificateDto);
        SingleFieldValidationErrorResponse errorResponse = getCertificateZeroPriceResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithZeroDuration() {
        GiftCertificateDto certificateDtoToCreate = getGiftCertificateDtoWithZeroDuration();
        String jsonCertificateToCreate = mapper.writeValueAsString(certificateDtoToCreate);
        SingleFieldValidationErrorResponse errorResponse = getCertificateZeroDurationResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToCreate))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkUpdateGiftCertificateShouldReturnErrorResponseWithSimilarParamsExist() {
        UpdateGiftCertificateDto updateCertificateDto = getUpdateGiftCertificateDtoWithExistingInDbFields();
        String jsonCertificateToUpdate = mapper.writeValueAsString(updateCertificateDto);
        ErrorResponse response = getCertificateCannotUpdateExistResponse();
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(patch("/certificates/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCertificateToUpdate))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteGiftCertificateShouldReturnResponseWithDeletedId() {
        ModificationResponse modificationResponse = getCertificateDeletedResponse();
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(delete("/certificates/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonModificationResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteGiftCertificateShouldReturnErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = getCertificateCannotDeleteIdNotFound();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(delete("/certificates/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteGiftCertificateShouldReturnErrorResponseWithIncorrectId() {
        ErrorResponse errorResponse = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(delete("/certificates/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }
}