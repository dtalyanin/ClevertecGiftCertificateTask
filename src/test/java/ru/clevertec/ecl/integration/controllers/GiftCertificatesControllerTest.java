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
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;
import ru.clevertec.ecl.models.responses.errors.MultipleFieldsValidationErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;

import java.time.Duration;
import java.util.List;

import static generators.factories.certificates.GiftCertificateDtoFactory.*;
import static generators.factories.certificates.UpdateGiftCertificateDtoFactory.*;
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
    void getAllGiftCertificatesWithFilteringShouldReturnCertificateDtosWithDefaultPagination() {
        List<GiftCertificateDto> certificateDtos = getGiftCertificateDtosFromDb();
        String jsonCertificateDtos = mapper.writeValueAsString(certificateDtos);

        mvc.perform(get("/certificates"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonCertificateDtos));
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
        ErrorResponse response = new ErrorResponse("Min ID value is 1", ErrorCode.INVALID_FIELD_VALUE.getCode());
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(get("/certificates/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkGetGiftCertificateByIdShouldErrorResponseWithIdNotFound() {
        GiftCertificateDto certificateDto = getSimpleGiftCertificateDtoWithTags();
        String jsonCertificateDto = mapper.writeValueAsString(certificateDto);
        ErrorResponse errorResponse = new ErrorResponse("Gift certificate with ID 10 not found in database",
                ErrorCode.CERTIFICATE_ID_NOT_FOUND.getCode());
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
        ModificationResponse modificationResponse = new ModificationResponse(4L, "Gift certificate added successfully");
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse("", "Gift certificate " +
                "name must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse("", "Gift certificate " +
                "description must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse(0, "Min price is 1 coin",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse(Duration.ofDays(0), "Min duration is 1 day",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        MultipleFieldsValidationErrorResponse errorResponse = new MultipleFieldsValidationErrorResponse(
                List.of(new SingleFieldValidationErrorResponse(null, "Gift certificate name must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode()),
                        new SingleFieldValidationErrorResponse(null, "Gift certificate description must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode()),
                        new SingleFieldValidationErrorResponse(null, "Price cannot be null", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode()),
                        new SingleFieldValidationErrorResponse(null, "Duration cannot be null", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode()))
        );
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

        ErrorResponse errorResponse = new ErrorResponse("Cannot add: gift certificate with similar name, description, " +
                "price and duration already exist in database",
                ErrorCode.CERTIFICATE_EXIST.getCode());
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
        ModificationResponse modificationResponse = new ModificationResponse(1L, "Gift certificate updated successfully");
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
        ErrorResponse errorResponse = new ErrorResponse("Cannot update: gift certificate with ID 10 not found in database",
                ErrorCode.CERTIFICATE_ID_NOT_FOUND.getCode());
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
        ErrorResponse response = new ErrorResponse("Min ID value is 1", ErrorCode.INVALID_FIELD_VALUE.getCode());
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
        ErrorResponse response = new ErrorResponse("Cannot update: no fields to update", ErrorCode.NO_CERTIFICATE_FIELDS_FOR_UPDATE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse("", "Gift certificate " +
                "name must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse("", "Gift certificate " +
                "description must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse(0, "Min price is 1 coin",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        SingleFieldValidationErrorResponse errorResponse = new SingleFieldValidationErrorResponse(Duration.ofDays(0), "Min duration is 1 day",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
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
        ErrorResponse response = new ErrorResponse("Cannot update: gift certificate with similar name, description, " +
                "price and duration already exist in database", ErrorCode.CERTIFICATE_EXIST.getCode());
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
        ModificationResponse modificationResponse = new ModificationResponse(3L, "Gift certificate deleted successfully");
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);

        mvc.perform(delete("/certificates/{id}", 3L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonModificationResponse));
    }

    @Test
    @SneakyThrows
    void checkDeleteGiftCertificateShouldReturnErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = new ErrorResponse("Cannot delete: gift certificate with ID 10 not found in database",
                ErrorCode.CERTIFICATE_ID_NOT_FOUND.getCode());
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(delete("/certificates/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }
}