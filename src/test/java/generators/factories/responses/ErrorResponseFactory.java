package generators.factories.responses;

import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;

public class ErrorResponseFactory {

    public static ErrorResponse getIncorrectIdResponse() {
        return new ErrorResponse("Min ID value is 1", ErrorCode.INVALID_FIELD_VALUE.getCode());
    }

    public static ErrorResponse getCertificateIdNotFoundResponse() {
        return new ErrorResponse("Gift certificate with ID 10 not found in database",
                ErrorCode.CERTIFICATE_ID_NOT_FOUND.getCode());
    }

    public static ErrorResponse getCertificateCannotAddExistResponse() {
        return new ErrorResponse("Cannot add: gift certificate with similar name, description, " +
                "price and duration already exist in database",
                ErrorCode.CERTIFICATE_EXIST.getCode());
    }

    public static ErrorResponse getCertificateCannotUpdateIdNotFound() {
        return new ErrorResponse("Cannot update: gift certificate with ID 10 not found in database",
                ErrorCode.CERTIFICATE_ID_NOT_FOUND.getCode());
    }

    public static ErrorResponse getCertificateCannotUpdateNoFieldsToUpdate() {
        return new ErrorResponse("Cannot update: no fields to update",
                ErrorCode.NO_CERTIFICATE_FIELDS_FOR_UPDATE.getCode());
    }

    public static ErrorResponse getCertificateCannotUpdateExistResponse() {
        return new ErrorResponse("Cannot update: gift certificate with similar name, description, " +
                "price and duration already exist in database", ErrorCode.CERTIFICATE_EXIST.getCode());
    }

    public static ErrorResponse getCertificateCannotDeleteIdNotFound() {
        return new ErrorResponse("Cannot delete: gift certificate with ID 10 not found in database",
                ErrorCode.CERTIFICATE_ID_NOT_FOUND.getCode());
    }

    public static ErrorResponse getTagIdNotFoundResponse() {
        return new ErrorResponse("Tag with ID 10 not found in database",
                ErrorCode.TAG_ID_NOT_FOUND.getCode());
    }

    public static ErrorResponse getTagCannotAddExistResponse() {
        return new ErrorResponse("Cannot add: tag with name 'Test tag' already exist in database",
                ErrorCode.TAG_NAME_EXIST.getCode());
    }

    public static ErrorResponse getTagCannotUpdateExistResponse() {
        return new ErrorResponse("Cannot update: tag with name 'Test tag 2' already exist in database",
                ErrorCode.TAG_NAME_EXIST.getCode());
    }

    public static ErrorResponse getTagCannotUpdateIdNotFound() {
        return new ErrorResponse("Cannot update: tag with ID 10 not found in database",
                ErrorCode.TAG_ID_NOT_FOUND.getCode());
    }

    public static ErrorResponse getTagCannotDeleteIdNotFound() {
        return new ErrorResponse("Cannot delete: tag with ID 10 not found in database",
                ErrorCode.TAG_ID_NOT_FOUND.getCode());
    }

    public static ErrorResponse getUserIdNotFoundResponse() {
        return new ErrorResponse("User with ID 10 not found in database",
                ErrorCode.USER_ID_NOT_FOUND.getCode());
    }
}
