package ru.clevertec.ecl.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private long id;
    @NotBlank(message = "Tag name must contain at least 1 character")
    private String name;
}
