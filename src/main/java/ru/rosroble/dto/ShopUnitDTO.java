package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rosroble.model.ShopUnitType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ShopUnitDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private String date;
    private String parentId;
    @NotNull
    private ShopUnitType type;
    private Long price;
    private List<ShopUnitDTO> children;
}
