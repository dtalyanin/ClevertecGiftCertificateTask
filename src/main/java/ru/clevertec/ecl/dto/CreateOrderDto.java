package ru.clevertec.ecl.dto;

import lombok.Data;


@Data
public class CreateOrderDto {
    private Long certificateId;
    private Integer quantity;
}
