package ru.clevertec.ecl.dto.orders;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ru.clevertec.ecl.utils.constants.MessageConstants.MIN_ID_MESSAGE;
import static ru.clevertec.ecl.utils.constants.MessageConstants.MIN_QUANTITY_MESSAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @Min(value = 1, message = MIN_ID_MESSAGE)
    private long certificateId;
    @Min(value = 1, message = MIN_QUANTITY_MESSAGE)
    private int quantity;
}
