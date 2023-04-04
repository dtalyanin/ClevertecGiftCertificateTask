package ru.clevertec.ecl.utils.mappers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.exceptions.InvalidItemException;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Validated
public abstract class TagMapper {
    private final Validator validator;

    public TagMapper() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public abstract TagDTO tagToDTO(Tag tag);
    public abstract @Valid Tag dtoToTag(TagDTO dto);
    public abstract List<TagDTO> allTagsToDTO(List<Tag> tags);
    public abstract List<@Valid Tag> allDTOToTags(List<TagDTO> dtoList);

    @AfterMapping
    protected void validateTag(@MappingTarget Tag tag) {
        Set<ConstraintViolation<Tag>> violations = validator.validate(tag);
        if (violations.size() != 0) {
            throw new InvalidItemException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")),
                    ErrorCode.INVALID_TAG_FIELD_VALUE);
        }
    }
}
