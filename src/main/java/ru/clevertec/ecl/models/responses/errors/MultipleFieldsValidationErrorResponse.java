package ru.clevertec.ecl.models.responses.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultipleFieldsValidationErrorResponse extends ValidationErrorResponse {
    private List<SingleFieldValidationErrorResponse> errors;
}