package ru.clevertec.ecl.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dao.TagsRepository;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.exceptions.ItemNotFoundException;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.TagsService;
import ru.clevertec.ecl.utils.mappers.TagMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagsServiceImpl implements TagsService {
    private final TagsRepository repository;
    private final TagMapper mapper;

    @Autowired
    public TagsServiceImpl(TagsRepository repository, TagMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> getAllTags(Pageable pageable) {
        List<Tag> allTags = repository.findAll(pageable).getContent();
        return mapper.tagsToDto(allTags);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getTagById(long id) {
        Optional<Tag> tag = repository.findById(id);
        if (tag.isEmpty()) {
            throw new ItemNotFoundException("Tag with ID " + id + " not found in database",
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        return mapper.tagToDto(tag.get());
    }

    @Override
    @Transactional
    public ModificationResponse addTag(TagDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ItemExistException("Cannot add: tag with name '" + dto.getName() +
                    "' already exist in database", ErrorCode.TAG_NAME_EXIST);
        }
        Tag tag = mapper.dtoToTag(dto);
        repository.save(tag);
        return new ModificationResponse(tag.getId(), "Tag added successfully");
    }

    @Override
    @Transactional
    public Set<Tag> addAllTagsIfNotExist(Set<Tag> tagsToAdd) {
        return tagsToAdd.stream()
                .map(this::createTagIfNotExist)
                .collect(Collectors.toSet());
    }

    private Tag createTagIfNotExist(Tag tag) {
        System.out.println(tag.getName());
        Optional<Tag> oTag = repository.findByName(tag.getName());
        System.out.println(oTag.isPresent());
        return oTag.orElseGet(() -> repository.save(tag));
    }

    @Override
    @Transactional
    public ModificationResponse updateTag(long id, TagDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ItemExistException("Cannot add: tag with name '" + dto.getName() +
                    "' already exist in database", ErrorCode.TAG_NAME_EXIST);
        }
        Optional<Tag> oTag = repository.findById(id);
        if (oTag.isEmpty()) {
            throw new ItemNotFoundException("Cannot update: tag with ID " + id + " not found",
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        mapper.updateTag(dto, oTag.get());
        return new ModificationResponse(id, "Tag updated successfully");

    }

    @Override
    @Transactional
    public ModificationResponse deleteTag(long id) {
        int deletedCount = repository.deleteById(id);
        if (deletedCount == 0) {
            throw new ItemNotFoundException("Cannot delete: tag with ID " + id + " not found",
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        return new ModificationResponse(id, "Tag deleted successfully");
    }
}
