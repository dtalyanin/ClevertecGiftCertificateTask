package ru.clevertec.ecl.models.criteries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortCriteria {
    private List<String> sort;
}
