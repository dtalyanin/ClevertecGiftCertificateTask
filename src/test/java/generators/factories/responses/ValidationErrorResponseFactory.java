package generators.factories.responses;

import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.errors.MultipleFieldsValidationErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;

import java.time.Duration;
import java.util.List;

public class ValidationErrorResponseFactory {

    public static SingleFieldValidationErrorResponse getCertificateEmptyNameResponse() {
        return new SingleFieldValidationErrorResponse("", "Gift certificate " +
                "name must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateEmptyDescriptionResponse() {
        return new SingleFieldValidationErrorResponse("", "Gift certificate " +
                "description must contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateZeroPriceResponse() {
        return new SingleFieldValidationErrorResponse(0, "Min price is 1 coin",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateZeroDurationResponse() {
        return new SingleFieldValidationErrorResponse(Duration.ofDays(0), "Min duration is 1 day",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateNullNameResponse() {
        return new SingleFieldValidationErrorResponse(null, "Gift certificate name must contain at " +
                "least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateNullDescriptionResponse() {
        return new SingleFieldValidationErrorResponse(null, "Gift certificate description must " +
                "contain at least 1 character", ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateNullPriceResponse() {
        return new SingleFieldValidationErrorResponse(null, "Price cannot be null",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getCertificateNullDurationResponse() {
        return new SingleFieldValidationErrorResponse(null, "Duration cannot be null",
                ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE.getCode());
    }

    public static MultipleFieldsValidationErrorResponse getCertificateNullNameDescriptionPriceDurationResponse() {
        return new MultipleFieldsValidationErrorResponse(List.of(getCertificateNullNameResponse(),
                getCertificateNullDescriptionResponse(),
                getCertificateNullPriceResponse(),
                getCertificateNullDurationResponse())
        );
    }

    public static SingleFieldValidationErrorResponse getTagEmptyNameResponse() {
        return new SingleFieldValidationErrorResponse(null,
                "Tag name must contain at least 1 character", ErrorCode.INVALID_TAG_FIELD_VALUE.getCode());
    }

    public static SingleFieldValidationErrorResponse getZeroOrderQuantityResponse() {
        return new SingleFieldValidationErrorResponse(0,
                "Min quantity is 1", ErrorCode.INVALID_ORDER_VALUE.getCode());
    }
}
