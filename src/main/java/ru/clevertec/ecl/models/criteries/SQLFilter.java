package ru.clevertec.ecl.models.criteries;

import lombok.Data;

import java.util.List;

@Data
public class SQLFilter {
    private final String sql;
    private final List<Object> filteringFields;
}
