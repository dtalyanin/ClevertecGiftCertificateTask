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
    TagDto convertTagToDto(Tag tag);
    @Valid Tag convertDtoToTag(TagDto dto);
    List<TagDto> convertTagsListToDtos(List<Tag> tags);
    Set<TagDto> convertTagsSetToDto(Set<Tag> tags);
    Set<@Valid Tag> convertTagDtosToTags(List<TagDto> dtos);
    @Valid Tag updateTag(TagDto dto, @MappingTarget Tag tag);
}
