package ru.clevertec.ecl.models.codes;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_FIELD_VALUE(40000),
    INVALID_CERTIFICATE_FIELD_VALUE(4000101),
    NO_CERTIFICATE_FIELDS_FOR_UPDATE(4000102),
    INVALID_TAG_FIELD_VALUE(4000201),
    CERTIFICATE_ID_NOT_FOUND(4040101),
    TAG_ID_NOT_FOUND(4040201),
    TAG_NAME_NOT_FOUND(4040202),
    CERTIFICATE_EXIST(42201),
    TAG_NAME_EXIST(42202);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
