package generators.factories;

import ru.clevertec.ecl.dto.TagDto;

import java.util.List;

public class TagDTOFactory {
    public static TagDto getSimpleTagDTO() {
        return new TagDto("Test tag");
    }

    public static TagDto getSimpleTagDTO2() {
        return new TagDto("Test tag 2");
    }

    public static TagDto getSimpleTagDTO3() {
        return new TagDto("Test tag 3");
    }

    public static TagDto getTagDTOWithoutName() {
        return new TagDto();
    }

    public static List<TagDto> getSimpleTagDTOs() {
        return List.of(getSimpleTagDTO(), getSimpleTagDTO2(), getSimpleTagDTO3());
    }
}
