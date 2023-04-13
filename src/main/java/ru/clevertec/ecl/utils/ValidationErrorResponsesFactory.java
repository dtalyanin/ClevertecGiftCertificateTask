package ru.clevertec.ecl.utils;

import jakarta.validation.ConstraintViolation;
import lombok.experimental.UtilityClass;
import org.springframework.validation.FieldError;
import ru.clevertec.ecl.dto.orders.CreateOrderDto;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.errors.MultipleFieldsValidationErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;
import ru.clevertec.ecl.models.responses.errors.ValidationErrorResponse;

import java.util.List;
import java.util.Set;

@UtilityClass
public class ValidationErrorResponsesFactory {
    public ValidationErrorResponse getResponseFromConstraints(Set<ConstraintViolation<?>> constraintViolations) {
        if (constraintViolations.size() == 1) {
            ConstraintViolation<?> cv = constraintViolations.iterator().next();
            return getSingleFieldValidationErrorResponse(cv);
        } else {
            List<SingleFieldValidationErrorResponse> validationResponses = constraintViolations.stream()
                    .map(ValidationErrorResponsesFactory::getSingleFieldValidationErrorResponse)
                    .toList();
            return new MultipleFieldsValidationErrorResponse(validationResponses);
        }
    }

    public ValidationErrorResponse getResponseFromErrors(List<FieldError> errors, Class<?> targetClass) {
        if (errors.size() == 1) {
            FieldError error = errors.get(0);
            return getSingleFieldValidationErrorResponse(error, targetClass);
        } else {
            List<SingleFieldValidationErrorResponse> validationResponses = errors.stream()
                    .map(error -> getSingleFieldValidationErrorResponse(error, targetClass))
                    .toList();
            return new MultipleFieldsValidationErrorResponse(validationResponses);
        }
    }

    private SingleFieldValidationErrorResponse getSingleFieldValidationErrorResponse(ConstraintViolation<?> cv) {
        return new SingleFieldValidationErrorResponse(cv.getInvalidValue(), cv.getMessage(),
                getIntErrorCodeDependsOnCause(cv.getLeafBean().getClass()));
    }

    private SingleFieldValidationErrorResponse getSingleFieldValidationErrorResponse(FieldError error, Class<?> targetClass) {
        return new SingleFieldValidationErrorResponse(error.getRejectedValue(), error.getDefaultMessage(),
                getIntErrorCodeDependsOnCause(targetClass));
    }

    private int getIntErrorCodeDependsOnCause(Class<?> causeClass) {
        ErrorCode errorCode;
        if (causeClass == GiftCertificate.class) {
            errorCode = ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE;
        } else if (causeClass == Tag.class) {
            errorCode = ErrorCode.INVALID_TAG_FIELD_VALUE;
        } else if (causeClass == CreateOrderDto.class) {
            errorCode = ErrorCode.INVALID_ORDER_VALUE;
        } else {
            errorCode = ErrorCode.INVALID_FIELD_VALUE;
        }
        return errorCode.getCode();
    }
}
