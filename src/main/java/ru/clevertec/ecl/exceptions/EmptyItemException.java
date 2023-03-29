package ru.clevertec.ecl.exceptions;

import ru.clevertec.ecl.models.codes.ErrorCode;

public class EmptyItemException extends ItemException {
    public EmptyItemException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
