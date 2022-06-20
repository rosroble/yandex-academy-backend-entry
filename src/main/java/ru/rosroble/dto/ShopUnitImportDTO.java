package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rosroble.model.ShopUnitType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ShopUnitImportDTO {
    @NotNull(message = "Id should not be null or empty")
    private UUID id;
    @NotBlank(message = "Name should not be null or empty")
    private String name;
    private UUID parentId;
    @NotNull
    private ShopUnitType type;
    @PositiveOrZero(message = "Price must be >= 0")
    private Long price;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopUnitImportDTO that = (ShopUnitImportDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
