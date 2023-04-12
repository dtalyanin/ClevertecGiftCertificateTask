package ru.clevertec.ecl.models.criteries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {
    private List<String> tags;
    private String name;
    private String description;
}
