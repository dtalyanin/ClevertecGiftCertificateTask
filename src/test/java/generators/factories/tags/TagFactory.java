package generators.factories.tags;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.models.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@UtilityClass
public class TagFactory {
    public Tag getSimpleTag() {
        return new Tag(1L, "Test tag", Collections.emptySet());
    }

    public Tag getSimpleTag2() {
        return new Tag(2L, "Test tag 2", Collections.emptySet());
    }

    public Tag getSimpleTag3() {
        return new Tag(3L, "Test tag 3", Collections.emptySet());
    }

    public Tag getSimpleTagWithoutId() {
        return new Tag(null, "Test tag", Collections.emptySet());
    }

    public Tag getSimpleTagWithoutId2() {
        return new Tag(null, "Test tag 2", Collections.emptySet());
    }

    public Tag getSimpleTagWithoutId3() {
        return new Tag(null, "Test tag 3", Collections.emptySet());
    }

    public Set<Tag> getDifferentTagsSet() {
        return Set.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }

    public List<Tag> getDifferentTagsList() {
        return List.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }

    public  Set<Tag> getDifferentTagsWithoutId() {
        return Set.of(getSimpleTagWithoutId(), getSimpleTagWithoutId2(), getSimpleTagWithoutId3());
    }

    public static Set<Tag> getDifferentTagsWithoutIdSet() {
        return Set.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }
}
