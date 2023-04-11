package ru.clevertec.ecl.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.UpdateGiftCertificateDto;
import ru.clevertec.ecl.models.criteries.FilterCriteria;
import ru.clevertec.ecl.models.criteries.PaginationCriteria;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.criteries.SortCriteria;
import ru.clevertec.ecl.services.GiftCertificatesService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/certificates")
@Validated
public class GiftCertificatesControllers {

    private final GiftCertificatesService service;

    @Autowired
    public GiftCertificatesControllers(GiftCertificatesService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<GiftCertificateDto>> getAllGiftCertificates(FilterCriteria filter, SortCriteria sorting,
                                                                    @Valid PaginationCriteria pagination) {
        return ResponseEntity.ok(service.getAllGiftCertificates(filter, sorting, pagination));
    }

    @GetMapping("/{id}")
    ResponseEntity<GiftCertificateDto> getGiftCertificateById(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long id) {
        return ResponseEntity.ok(service.getGiftCertificateById(id));
    }

    @PostMapping
    public ResponseEntity<ModificationResponse> addGiftCertificate(@RequestBody GiftCertificateDto dto) {
        ModificationResponse response = service.addGiftCertificate(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ModificationResponse> updateTag(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long id,
            @RequestBody UpdateGiftCertificateDto dto) {
        return ResponseEntity.ok(service.updateGiftCertificate(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ModificationResponse> deleteTagById(
            @PathVariable @Min(value = 1, message = "Min ID value is 1") long id) {
        return ResponseEntity.ok(service.deleteGiftCertificate(id));
    }
}
