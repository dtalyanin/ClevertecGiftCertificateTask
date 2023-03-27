package ru.clevertec.ecl.controllers;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.dto.TagDTO;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.services.TagsService;

import java.net.URI;
import java.util.List;

/**
 * Controller for performing operations with tags for gift certificates
 */
@RestController
@RequestMapping("/tags")
@Validated
public class TagsController {

    private final TagsService service;

    @Autowired
    public TagsController(TagsService service) {
        this.service = service;
    }

    /**
     * Get all available tags for gift certificates
     *
     * @return List of tags
     */
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(service.getAllTags());
    }

    /**
     * Get tag by its ID
     *
     * @param id tag ID to search
     * @return tag with specified id
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable @Min(value = 1, message = "Min ID value is 1") long id) {
        return ResponseEntity.ok(service.getTagById(id));
    }

    /**
     * Add new tag
     *
     * @param tag tag to add
     * @return response with generated ID,message about performing operation and location of new tag
     */
    @PostMapping
    public ResponseEntity<ModificationResponse> addTag(@RequestBody TagDTO tag) {
        ModificationResponse response = service.addTag(tag);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Change tag fields with specified ID
     *
     * @param id  tag ID to update
     * @param tag tag with new values for update
     * @return response with updated ID and message about performing operation
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ModificationResponse> updateTag(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long id, @RequestBody TagDTO tag) {
        return ResponseEntity.ok(service.updateTag(id, tag));
    }

    /**
     * Delete tag with specified ID
     *
     * @param id tag ID to delete
     * @return response with deleted ID and message about performing operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ModificationResponse> deleteTagById(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long id) {
        return ResponseEntity.ok(service.deleteTag(id));
    }
}
