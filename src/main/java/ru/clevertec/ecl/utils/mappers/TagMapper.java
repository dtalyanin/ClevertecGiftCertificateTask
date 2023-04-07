package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.models.Tag;

import java.util.List;

@Validated
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TagMapper {
    public abstract TagDto tagToDto(Tag tag);
    public abstract @Valid Tag dtoToTag(TagDto dto);
    public abstract List<TagDto> tagsToDto(List<Tag> tags);
    public abstract List<@Valid Tag> TagDtosToTag(List<TagDto> tagDtos);
}
