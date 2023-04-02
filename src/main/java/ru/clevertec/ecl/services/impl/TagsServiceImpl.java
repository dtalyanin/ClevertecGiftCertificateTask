package ru.clevertec.ecl.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.TagsDAO;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.services.TagsService;
import ru.clevertec.ecl.utils.mappers.TagMapper;

import java.util.*;

@Service
public class TagsServiceImpl implements TagsService {
    private final TagsDAO dao;
    private final TagMapper mapper;

    @Autowired
    public TagsServiceImpl(TagsDAO dao, TagMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        List<Tag> allTags = dao.getAllTags();
        return mapper.allTagsToDTO(allTags);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO getTagById(long id) {
        Optional<Tag> tag = dao.getTagById(id);
        if (tag.isEmpty()) {
            throw new ItemNotFoundException("Tag with ID " + id + " not found in database",
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        return mapper.tagToDTO(tag.get());
    }

    @Override
    @Transactional
    public ModificationResponse addTag(TagDTO dto) {
        Tag tag = mapper.dtoToTag(dto);
        try {
            long generatedId = dao.addTag(tag);
            return new ModificationResponse(generatedId, "Tag added successfully");
        } catch (ConstraintViolationException e) {
            throw new ItemExistException("Cannot add: tag with name '" + tag.getName() +
                    "' already exist in database", ErrorCode.TAG_NAME_EXIST);
        }
    }

    @Override
    @Transactional
    public List<Long> addAllTagsIfNotExist(List<Tag> tags) {
        Set<Tag> tagsSet = new HashSet<>(tags);
        List<Long> ids = new ArrayList<>();
        tagsSet.stream()
                .filter(tag -> {
                    Optional<Tag> oTag = dao.getTagByName(tag.getName());
                    if (oTag.isEmpty()) {
                        return true;
                    } else {
                        ids.add(oTag.get().getId());
                        return false;
                    }
                })
                .map(dao::addTag)
                .forEach(ids::add);
        return ids;
    }

    @Override
    @Transactional
    public ModificationResponse updateTag(long id, TagDTO dto) {
        Tag tag = mapper.dtoToTag(dto);
        try {
            int updatedRows = dao.updateTag(id, tag);
            if (updatedRows == 0) {
                throw new ItemNotFoundException("Cannot update: tag with ID " + id + " not found",
                        ErrorCode.TAG_ID_NOT_FOUND);
            }
            return new ModificationResponse(id, "Tag updated successfully");
        } catch (DuplicateKeyException e) {
            throw new ItemExistException("Cannot update: tag with name '" + tag.getName() +
                    "' already exist in database", ErrorCode.TAG_NAME_EXIST);
        }
    }

    @Override
    @Transactional
    public ModificationResponse deleteTag(long id) {
        int deletedRows = dao.deleteTag(id);
        if (deletedRows == 0) {
            throw new ItemNotFoundException("Cannot delete: tag with ID " + id + " not found",
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        return new ModificationResponse(id, "Tag deleted successfully");
    }
}
