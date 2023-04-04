package generators.factories;

import ru.clevertec.ecl.models.Tag;

import java.util.List;
import java.util.Set;

public class TagFactory {
    public static Tag getSimpleTag() {
        return new Tag(1, "Test tag");
    }

    public static Tag getSimpleTagWithoutId() {
        return new Tag(0, "Test tag");
    }


    public static Tag getSimpleTag2() {
        return new Tag(2, "Test tag 2");
    }

    public static Tag getSimpleTag3() {
        return new Tag(3, "Test tag 3");
    }

    public static List<Tag> getSimpleTags() {
        return List.of(getSimpleTag(), getSimpleTag());
    }

    public static List<Tag> getDifferentTags() {
        return List.of(getSimpleTag(), getSimpleTag2(), getSimpleTag3());
    }

    public static Set<Tag> getDifferentTagsWithoutId() {
        return Set.of(new Tag(0, "Test tag"), new Tag(0, "Test tag 2"),
                new Tag(0, "Test tag 3"));
    }
}
