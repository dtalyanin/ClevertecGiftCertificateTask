package ru.clevertec.ecl.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.exceptions.*;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.ErrorResponse;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> findValidationExceptionInParameters(ConstraintViolationException e) {
        ConstraintViolation<?> constraintViolation = e.getConstraintViolations().iterator().next();
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        ErrorCode errorCode;
        if (constraintViolation.getLeafBean().getClass() == GiftCertificate.class) {
            errorCode = ErrorCode.INVALID_CERTIFICATE_FIELD_VALUE;
        } else if (constraintViolation.getLeafBean().getClass() == Tag.class) {
            errorCode = ErrorCode.INVALID_TAG_FIELD_VALUE;
        } else {
            errorCode = ErrorCode.INVALID_FIELD_VALUE;
        }
        ErrorResponse errorResponse = new ErrorResponse(message, errorCode.getCode());
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
