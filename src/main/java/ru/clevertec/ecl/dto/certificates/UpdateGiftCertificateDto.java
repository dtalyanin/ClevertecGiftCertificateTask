package ru.clevertec.ecl.dto.certificates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.dto.TagDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGiftCertificateDto {
    private String name;
    private String description;
    private List<TagDto> tags;
    private BigDecimal price;
    private Long duration;
}
