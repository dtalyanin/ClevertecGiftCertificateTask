package ru.clevertec.ecl.exceptions;

import lombok.Getter;
import ru.clevertec.ecl.models.codes.ErrorCode;

@Getter
public class ItemNotFoundException extends ItemException {
    public ItemNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
