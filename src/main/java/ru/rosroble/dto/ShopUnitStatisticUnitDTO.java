package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.rosroble.model.ShopUnitType;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ShopUnitStatisticUnitDTO {

    private String id;
    private String name;
    private String parentId;
    private ShopUnitType type;
    private Long price;
    private Date date;
}
