package ru.clevertec.ecl.services;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.responses.ModificationResponse;

import java.util.List;
import java.util.Set;

public interface TagsService {
    List<TagDto> getAllTags(Pageable pageable);
    TagDto getTagById(long id);
    ModificationResponse addTag(TagDto dto);
    Set<Tag> addAllTagsIfNotExist(Set<Tag> tags);
    ModificationResponse updateTag(long id, TagDto dto);
    ModificationResponse deleteTag(long id);
    TagDto getMostWidelyUsedTagOfUserWithHighestOrdersCost();
}
