package ru.clevertec.ecl.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateOrderDto {
    @NotNull
    @Min(value = 1, message = "Min ID value is 1")
    private Long certificateId;
    @Min(value = 1, message = "Min quantity is 1")
    private Integer quantity;
}
