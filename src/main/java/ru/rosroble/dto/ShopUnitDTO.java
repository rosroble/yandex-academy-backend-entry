package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rosroble.model.ShopUnitType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ShopUnitDTO {
    @NotBlank
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private String date;
    private UUID parentId;
    @NotNull
    private ShopUnitType type;
    private Long price;
    private List<ShopUnitDTO> children;
}
