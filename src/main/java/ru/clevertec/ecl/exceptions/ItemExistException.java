package ru.clevertec.ecl.exceptions;

import ru.clevertec.ecl.models.codes.ErrorCode;

public class ItemExistException extends ItemException {
    public ItemExistException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
