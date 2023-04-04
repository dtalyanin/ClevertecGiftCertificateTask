package generators.factories;

import ru.clevertec.ecl.models.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TagFactory {
    public static Tag getSimpleTag() {
        return new Tag(1L, "Test tag", Collections.emptySet());
    }

    public static Tag getSimpleTagWithoutId() {
        return new Tag(0, "Test tag", Collections.emptySet());
    }

    public static Tag getSimpleTag2() {
        return new Tag(2L, "Test tag 2", Collections.emptySet());
    }

    public static Tag getSimpleTag3() {
        return new Tag(3L, "Test tag 3", Collections.emptySet());
    }

    public static List<Tag> getDifferentTags() {
        return List.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }

    public static Set<Tag> getDifferentTagsSet() {
        return Set.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }

    public static List<Tag> getDifferentTagsWithoutId() {
        return List.of(getSimpleTagWithoutId(), new Tag(0, "Test tag 2", Collections.emptySet()),
                new Tag(0, "Test tag 3", Collections.emptySet()));
    }

    public static Set<Tag> getDifferentTagsWithoutIdSet() {
        return Set.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }
}
