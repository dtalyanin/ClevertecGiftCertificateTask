package ru.clevertec.ecl.dao;

import ru.clevertec.ecl.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagsDAO {
    List<Tag> getAllTags();
    Optional<Tag> getTagById(long id);
    Optional<Tag> getTagByName(String name);
    Tag addTag(Tag tag);
    boolean updateTag(long id, Tag tag);
    boolean deleteTag(long id);
}
