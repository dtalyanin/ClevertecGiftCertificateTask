package generators.factories.tags;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.dto.TagDto;

import java.util.List;
import java.util.Set;

@UtilityClass
public class TagDtoFactory {

    public TagDto getSimpleTagDto() {
        return new TagDto("Test tag");
    }

    public TagDto getSimpleTagDto2() {
        return new TagDto("Test tag 2");
    }

    public TagDto getSimpleTagDto3() {
        return new TagDto("Test tag 3");
    }

    public TagDto getTagDtoWithoutName() {
        return new TagDto();
    }

    public List<TagDto> getSimpleTagDtosList() {
        return List.of(getSimpleTagDto(), getSimpleTagDto2(), getSimpleTagDto3());
    }

    public Set<TagDto> getSimpleTagDtosSet() {
        return Set.of(getSimpleTagDto(), getSimpleTagDto2(), getSimpleTagDto3());
    }
}
