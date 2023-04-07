package ru.clevertec.ecl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModGiftCertificateDTO {
    private String name;
    private String description;
    private List<TagDto> tags;
    private BigDecimal price;
    private Long duration;
}
