package ru.clevertec.ecl.utils;

import jakarta.persistence.criteria.Join;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.models.GiftCertificate;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.utils.constants.GiftCertificateParams;

import java.util.ArrayList;
import java.util.List;

import static ru.clevertec.ecl.utils.constants.GiftCertificateParams.*;

@UtilityClass
public class SpecificationHelper {

    public List<Specification<GiftCertificate>> getSpecificationsFromFilter(FilterCriteria filter) {
        List<Specification<GiftCertificate>> specifications = new ArrayList<>();
        if (filter != null) {
            if (checkFieldIsNotEmpty(filter.getName())) {
                Specification<GiftCertificate> nameSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(CERTIFICATE_NAME)), getFieldLikeLowerStr(filter.getName()));
                specifications.add(nameSpecification);
            }
            if (checkFieldIsNotEmpty(filter.getDescription())) {
                Specification<GiftCertificate> descriptionSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get(CERTIFICATE_DESCRIPTION)), getFieldLikeLowerStr(filter.getDescription()));
                specifications.add(descriptionSpecification);
            }
            if (filter.getTags() != null) {
                filter.getTags().stream().filter(SpecificationHelper::checkFieldIsNotEmpty).forEach(tag -> {
                    Specification<GiftCertificate> tagSpecification = (root, query, criteriaBuilder) -> {
                        Join<GiftCertificate, Tag> tagsJoin = root.join(CERTIFICATE_TAGS);
                        return criteriaBuilder.equal(
                                criteriaBuilder.lower(tagsJoin.get(CERTIFICATE_NAME)), tag.toLowerCase());
                    };
                    specifications.add(tagSpecification);
                });
            }
        }
        return specifications;
    }

    private boolean checkFieldIsNotEmpty(String field) {
        return field != null && !field.isBlank();
    }

    private String getFieldLikeLowerStr(String field) {
        return PERCENT + field.toLowerCase() + PERCENT;
    }
}
