package generators.factories;

import ru.clevertec.ecl.models.criteries.SQLFilter;
import ru.clevertec.ecl.utils.constants.GiftCertificateParams;
import ru.clevertec.ecl.utils.constants.GiftCertificatesSQL;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static ru.clevertec.ecl.utils.constants.GiftCertificateParams.*;
import static ru.clevertec.ecl.utils.constants.GiftCertificatesSQL.*;

public class SQLFilterFactory {
    public static SQLFilter getSQLFilterByTag() {
        return new SQLFilter(WHERE_CLAUSE + FILTER_BY_TAG_NAME, Map.of("tag", "Test tag"));
    }

    public static SQLFilter getSQLFilterByName() {
        return new SQLFilter(WHERE_CLAUSE + FILTER_BY_GIFT_CERTIFICATE_NAME, Map.of("name", "%t 2%"));
    }

    public static SQLFilter getSQLFilterByDescription() {
        return new SQLFilter(WHERE_CLAUSE + FILTER_BY_GIFT_CERTIFICATE_DESCRIPTION, Map.of("description", "%ion 2%"));
    }

    public static SQLFilter getSQLFilterByTagNameDescription() {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("tag", "Test tag");
        values.put("name", "%test%");
        values.put("description", "%ion 2%");
        return new SQLFilter(WHERE_CLAUSE + FILTER_BY_TAG_NAME + AND_CLAUSE + FILTER_BY_GIFT_CERTIFICATE_NAME
                + AND_CLAUSE + FILTER_BY_GIFT_CERTIFICATE_DESCRIPTION, values);
    }

    public static SQLFilter getSQLFilterWithoutFields() {
        return new SQLFilter(EMPTY_STR, Collections.emptyMap());
    }
}
