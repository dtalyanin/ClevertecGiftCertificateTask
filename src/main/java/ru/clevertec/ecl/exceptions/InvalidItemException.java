package ru.clevertec.ecl.exceptions;

import ru.clevertec.ecl.models.codes.ErrorCode;

public class InvalidItemException extends ItemException {
    public InvalidItemException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
