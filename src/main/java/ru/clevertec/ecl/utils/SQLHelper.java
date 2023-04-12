package ru.clevertec.ecl.utils;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.SQLFilter;
import ru.clevertec.ecl.models.criteries.SortCriteria;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.clevertec.ecl.utils.constants.GiftCertificateParams.*;
import static ru.clevertec.ecl.utils.constants.GiftCertificatesSQL.*;

@UtilityClass
public class SQLHelper {
    public SQLFilter getSQLFilter(FilterCriteria filter) {
        Map<String, Object> paramValues = new LinkedHashMap<>();
        StringBuilder sb = new StringBuilder();
//        if (filter != null) {
//            if (filter.getTag() != null) {
//                sb.append(FILTER_BY_TAG_NAME);
//                paramValues.put(CERTIFICATE_TAG, filter.getTag());
//            }
//            if (filter.getName() != null) {
//                sb.append(getAndClauseIfFilterAlreadyExist(sb));
//                sb.append(FILTER_BY_GIFT_CERTIFICATE_NAME);
//                paramValues.put(CERTIFICATE_NAME, PERCENT + filter.getName() + PERCENT);
//            }
//            if (filter.getDescription() != null) {
//                sb.append(getAndClauseIfFilterAlreadyExist(sb));
//                sb.append(FILTER_BY_GIFT_CERTIFICATE_DESCRIPTION);
//                paramValues.put(CERTIFICATE_DESCRIPTION, PERCENT + filter.getDescription() + PERCENT);
//            }
//            if (!sb.isEmpty()) {
//                sb.insert(0, WHERE_CLAUSE);
//            }
//        }
        return new SQLFilter(sb.toString(), paramValues);
    }

    private String getAndClauseIfFilterAlreadyExist(StringBuilder sb) {
        return sb.isEmpty() ? EMPTY_STR : AND_CLAUSE;
    }

    public String getSQLOrder(SortCriteria criteria) {
        String sortingOrder = EMPTY_STR;
        if (criteria != null && criteria.getSort() != null && criteria.getSort().size() != 0) {
            String sorting = criteria.getSort().stream()
                    .map(SQLHelper::getSplitParams)
                    .filter(SQLHelper::isValidParams)
                    .map(SQLHelper::getFieldAndItsOrder)
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.joining(", "));
            if (!sorting.isBlank()) {
                sortingOrder = ORDER_BY_CLAUSE + sorting;
            }
        }
        return sortingOrder;
    }

    private List<String> getSplitParams(String params) {
        return List.of(params.split(ORDER_DELIMITER));
    }

    private boolean isValidParams(List<String> params) {
        return params.size() > 0 && params.get(INDEX_OF_COLUMN_WITH_NAME).matches("[A-Za-z]+");
    }

    private String getFieldAndItsOrder(List<String> params) {
        StringBuilder sb = new StringBuilder();
        if (CERTIFICATE_NAME.equalsIgnoreCase(params.get(INDEX_OF_COLUMN_WITH_NAME))) {
            sb.append(CERTIFICATE_NAME_WIH_TABLE);
        } else if (CERTIFICATE_DATE.equalsIgnoreCase(params.get(0))) {
            sb.append(CERTIFICATE_CREATE_DATE_WIH_TABLE);
        }
        if (!sb.isEmpty() && params.size() > 1 &&
                DESC_ORDER.equalsIgnoreCase(params.get(INDEX_OF_COLUMN_WITH_ORDERING))) {
            sb.append(DESC_FOR_APPEND);
        }
        return sb.toString();
    }
}
