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
import ru.clevertec.ecl.utils.PageableHelper;
import ru.clevertec.ecl.utils.mappers.TagMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.utils.constants.MessageConstants.*;

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
        pageable = PageableHelper.setPageableUnsorted(pageable);
        List<Tag> allTags = repository.findAll(pageable).getContent();
        return mapper.convertTagsListToDtos(allTags);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getTagById(long id) {
        Optional<Tag> tag = repository.findById(id);
        if (tag.isEmpty()) {
            throw new ItemNotFoundException(TAG_ID_START + id + NOT_FOUND, ErrorCode.TAG_ID_NOT_FOUND);
        }
        return mapper.convertTagToDto(tag.get());
    }

    @Override
    @Transactional
    public ModificationResponse addTag(TagDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ItemExistException(CANNOT_ADD +TAG_NAME_START+ dto.getName() + EXIST, ErrorCode.TAG_NAME_EXIST);
        }
        Tag tag = mapper.convertDtoToTag(dto);
        long generatedId = repository.save(tag).getId();
        return new ModificationResponse(generatedId, TAG_ADDED);
    }

    @Override
    @Transactional
    public Set<Tag> addAllTagsIfNotExist(Set<Tag> tagsToAdd) {
        return tagsToAdd.stream()
                .map(this::createTagIfNotExist)
                .collect(Collectors.toSet());
    }

    private Tag createTagIfNotExist(Tag tag) {
        Optional<Tag> oTag = repository.findByName(tag.getName());
        return oTag.orElseGet(() -> repository.save(tag));
    }

    @Override
    @Transactional
    public ModificationResponse updateTag(long id, TagDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ItemExistException(CANNOT_UPDATE + TAG_NAME_START + dto.getName() + EXIST,
                    ErrorCode.TAG_NAME_EXIST);
        }
        Optional<Tag> oTag = repository.findById(id);
        if (oTag.isEmpty()) {
            throw new ItemNotFoundException(CANNOT_UPDATE + TAG_ID_START_L + id + NOT_FOUND,
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        Tag tag = oTag.get();
        mapper.updateTag(dto, tag);
        repository.save(tag);
        return new ModificationResponse(id, TAG_UPDATED);
    }

    @Override
    @Transactional
    public ModificationResponse deleteTag(long id) {
        int deletedCount = repository.deleteById(id);
        if (deletedCount == 0) {
            throw new ItemNotFoundException(CANNOT_DELETE + TAG_ID_START_L + id + NOT_FOUND,
                    ErrorCode.TAG_ID_NOT_FOUND);
        }
        return new ModificationResponse(id, TAG_DELETED);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getMostWidelyUsedTagOfUserWithHighestOrdersCost() {
        return mapper.convertTagToDto(repository.findMostWidelyUsedTagOfUserWithHighestOrdersCost());
    }
}
