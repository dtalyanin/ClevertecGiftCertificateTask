package ru.clevertec.ecl.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@Validated
public class GiftCertificate {
    private long id;
    @NotBlank(message = "Gift certificate name must contain at least 1 character")
    private String name;
    @NotBlank(message = "Gift certificate description must contain at least 1 character")
    private  String description;
    @Singular(ignoreNullCollections = true)
    private  List<Tag> tags;
    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Min price is 1 coin")
    private Long price;
    @NotNull(message = "Duration cannot be null")
    private Duration duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    public GiftCertificate(long id, String name, String description, List<Tag> tags, Long price, Duration duration,
                           LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = new ArrayList<>(tags);
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
