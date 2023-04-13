package ru.clevertec.ecl.dto.orders;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @Min(value = 1, message = "Min ID value is 1")
    private long certificateId;
    @Min(value = 1, message = "Min quantity is 1")
    private int quantity;
}
