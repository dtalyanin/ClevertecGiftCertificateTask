package generators.factories.responses;

import ru.clevertec.ecl.models.responses.ModificationResponse;

public class ModificationResponseFactory {

    public static ModificationResponse getCertificateAddedResponse() {
        return new ModificationResponse(4L, "Gift certificate added successfully");
    }

    public static ModificationResponse getCertificateUpdatedResponse() {
        return new ModificationResponse(1L, "Gift certificate updated successfully");
    }

    public static ModificationResponse getCertificateDeletedResponse() {
        return new ModificationResponse(3L, "Gift certificate deleted successfully");
    }

    public static ModificationResponse getTagAddedResponse() {
        return new ModificationResponse(4L, "Tag added successfully");
    }

    public static ModificationResponse getTagUpdatedResponse() {
        return new ModificationResponse(1L, "Tag updated successfully");
    }

    public static ModificationResponse getTagDeletedResponse() {
        return new ModificationResponse(3L, "Tag deleted successfully");
    }

    public static ModificationResponse getOrderAddedResponse() {
        return new ModificationResponse(4L, "Order added successfully");
    }
}
