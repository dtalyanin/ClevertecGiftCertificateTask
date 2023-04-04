package ru.clevertec.ecl.models.criteries;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SQLFilter {
    private final String sql;
    private final Map<String, Object> filteringFields;
}
