package ru.clevertec.ecl.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.exceptions.*;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;
import ru.clevertec.ecl.models.responses.errors.ValidationErrorResponse;

import static ru.clevertec.ecl.utils.ValidationErrorResponsesFactory.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> findValidationExceptionInParameters(ConstraintViolationException e) {
        ValidationErrorResponse errorResponse = getResponseFromConstraints(e.getConstraintViolations());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> findBookException(MethodArgumentNotValidException e) {
        ValidationErrorResponse errorResponse = getResponseFromErrors(e.getFieldErrors(), e.getTarget().getClass());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDBException(ItemNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getErrorCode().getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemExistException.class)
    public ResponseEntity<ErrorResponse> handleDBException(ItemExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getErrorCode().getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({EmptyItemException.class, InvalidItemException.class})
    public ResponseEntity<ErrorResponse> handleDBException(ItemException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getErrorCode().getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
