package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ShopUnitStatisticResponseDTO {
    private List<ShopUnitStatisticUnitDTO> items;
}
