package ru.clevertec.ecl.models.criteries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {
    private String tag;
    private String name;
    private String description;
}
