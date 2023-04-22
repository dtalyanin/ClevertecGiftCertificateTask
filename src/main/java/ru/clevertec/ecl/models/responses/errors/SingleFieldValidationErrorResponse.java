package ru.clevertec.ecl.models.responses.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleFieldValidationErrorResponse extends ValidationErrorResponse {
    private Object invalidValue;
    private String errorMessage;
    private int errorCode;
}
