package ru.clevertec.ecl.services;

import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.Tag;

import java.util.List;

public interface TagsService {
    List<TagDTO> getAllTags();
    TagDTO getTagById(long id);
    ModificationResponse addTag(TagDTO dto);
    List<Long> addAllTagsIfNotExist(List<Tag> tags);
    ModificationResponse updateTag(long id, TagDTO dto);
    ModificationResponse deleteTag(long id);
}
