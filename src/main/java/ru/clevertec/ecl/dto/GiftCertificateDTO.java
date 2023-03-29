package ru.clevertec.ecl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateDTO {
    private String name;
    private String description;
    private List<TagDTO> tags;
    private BigDecimal price;
    private Long duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
