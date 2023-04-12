package ru.clevertec.ecl.dto;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> {
    T getId();
}