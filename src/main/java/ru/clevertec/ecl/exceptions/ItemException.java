package ru.clevertec.ecl.exceptions;

import lombok.Getter;
import ru.clevertec.ecl.models.codes.ErrorCode;

@Getter
public class ItemException extends IllegalArgumentException {
    private final ErrorCode errorCode;

    public ItemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
