package ru.rosroble.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rosroble.dto.*;
import ru.rosroble.service.ShopUnitService;

import javax.validation.Valid;
import ru.rosroble.exception.ValidationException;

import java.util.Date;

@RestController
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
        if (!shopUnitService.delete(uuid)) {
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
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date) {
        ShopUnitStatisticResponseDTO response = shopUnitService.sales(date);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/test")
    public ResponseEntity<Void> test(@RequestBody @Valid ShopUnitImportRequestDTO dto) {
      //  System.out.println(dto.getUpdateDate());
        return ResponseEntity.ok().build();
    }
}
