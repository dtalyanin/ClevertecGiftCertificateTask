package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.models.Tag;

import java.util.List;
import java.util.Set;

@Validated
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    TagDto tagToDto(Tag tag);
    @Valid Tag dtoToTag(TagDto dto);
    List<TagDto> tagsToDto(List<Tag> tags);
    Set<@Valid Tag> TagDtosToTag(List<TagDto> tagDtos);
    List<TagDto> tagsToDtos(Set<Tag> tags);
    @Valid Tag updateTag(TagDto dto, @MappingTarget Tag tag);
}
