package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.models.Tag;

import java.util.List;

@Validated
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    TagDTO tagToDTO(Tag tag);
    @Valid Tag dtoToTag(TagDTO dto);
    List<TagDTO> allTagsToDTO(List<Tag> tags);
    List<@Valid Tag> allDTOToTags(List<TagDTO> dtoList);
}
