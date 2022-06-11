package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rosroble.model.ShopUnit;
import ru.rosroble.model.ShopUnitType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Data
@AllArgsConstructor
public class ShopUnitImportDTO {
    @NotBlank(message = "Id should not be null or empty")
    private String id;
    @NotBlank(message = "Name should not be null or empty")
    private String name;
    private String parentId;
    @NotNull
    private ShopUnitType type;
    @PositiveOrZero(message = "Price must be >= 0")
    private Long price;

    public ShopUnit toShopUnit() {
        // ???
        return new ShopUnit(id, name, null, null, type, price, null);
    }

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
