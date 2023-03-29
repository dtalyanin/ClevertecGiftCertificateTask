package generators.factories;

import ru.clevertec.ecl.dto.TagDTO;

import java.util.List;

public class TagDTOFactory {
    public static TagDTO getSimpleTagDTO() {
        return new TagDTO("Test tag");
    }

    public static TagDTO getSimpleTagDTO2() {
        return new TagDTO("Test tag 2");
    }

    public static TagDTO getSimpleTagDTO3() {
        return new TagDTO("Test tag 3");
    }

    public static TagDTO getTagDTOWithoutName() {
        return new TagDTO();
    }

    public static List<TagDTO> getSimpleTagDTOs() {
        return List.of(getSimpleTagDTO(), getSimpleTagDTO2(), getSimpleTagDTO3());
    }
}
