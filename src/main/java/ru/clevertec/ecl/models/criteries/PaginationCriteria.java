package ru.clevertec.ecl.models.criteries;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginationCriteria {
    @Min(value = 0, message = "Start position for gift certificate is 0")
    private int offset = 0;
    @Min(value = 1, message = "Gift certificates count cannot be less than 1")
    @Max(value = 40, message = "Maximum gift certificates is 40 per page" )
    private int limit = 10;
}
