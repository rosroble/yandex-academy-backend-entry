package ru.rosroble.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.rosroble.dto.*;
import ru.rosroble.service.ShopUnitService;

import javax.validation.Valid;
import ru.rosroble.exception.ValidationException;
import ru.rosroble.validator.ValidDateRange;

import java.util.Date;

@RestController
@Validated
public class ShopUnitController {

    @Autowired
    ShopUnitService shopUnitService;

    @PostMapping(value="/imports", produces = "application/json")
    public ResponseEntity<?> imports(@RequestBody @Valid ShopUnitImportRequestDTO dto) {
        try {
            shopUnitService.importNewUnits(dto);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json")
                    .body(new ErrorDTO(400, e.getMessage()));
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String uuid) {
        if (!shopUnitService.deleteAndUpdateCategoryPrices(uuid)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<?> nodes(@PathVariable("id") String uuid) {
        ShopUnitDTO unitDto = shopUnitService.getNode(uuid);
        return unitDto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(unitDto);
    }

    @GetMapping("/sales")
    public ResponseEntity<?> sales(@RequestParam("date")
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                       @ValidDateRange Date date) {
        ShopUnitStatisticResponseDTO response = shopUnitService.sales(date);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/node/{id}/statistic")
    public ResponseEntity<?> statistic(@PathVariable("id") String id,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ValidDateRange Date dateStart,
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ValidDateRange Date dateEnd) {
        if (dateStart == null) dateStart = new Date(0);
        if (dateEnd == null) dateEnd = new Date(System.currentTimeMillis());
        System.out.println(dateStart);
        System.out.println(dateEnd);
        ShopUnitStatisticResponseDTO response = shopUnitService.statistic(id, dateStart, dateEnd);
        return ResponseEntity.ok().body(response);
    }

    // delete
    @PostMapping("/test")
    public ResponseEntity<Void> test(@RequestBody @Valid ShopUnitImportRequestDTO dto) {
        return ResponseEntity.ok().build();
    }
}
