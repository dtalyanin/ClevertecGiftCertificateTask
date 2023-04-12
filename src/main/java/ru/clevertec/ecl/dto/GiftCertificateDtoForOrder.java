package ru.clevertec.ecl.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiftCertificateDtoForOrder {
    private String name;
    private String description;
    private Long duration;
}
