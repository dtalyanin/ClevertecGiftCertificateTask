package generators.factories.tags;

import ru.clevertec.ecl.dto.TagDto;

import java.util.List;
import java.util.Set;

public class TagDtoFactory {

    public static TagDto getSimpleTagDto() {
        return new TagDto("Test tag");
    }

    public static TagDto getSimpleTagDto2() {
        return new TagDto("Test tag 2");
    }

    public static TagDto getSimpleTagDto3() {
        return new TagDto("Test tag 3");
    }

    public static TagDto getTagDtoWithoutName() {
        return new TagDto();
    }

    public static TagDto getSimpleTagDtoToCreate() {
        return new TagDto("Test tag created");
    }

    public static TagDto getSimpleTagDtoToUpdate() {
        return new TagDto("Test tag updated");
    }

    public static List<TagDto> getSimpleTagDtosList() {
        return List.of(getSimpleTagDto(), getSimpleTagDto2(), getSimpleTagDto3());
    }

    public static Set<TagDto> getSimpleTagDtosSet() {
        return Set.of(getSimpleTagDto(), getSimpleTagDto2(), getSimpleTagDto3());
    }
}
