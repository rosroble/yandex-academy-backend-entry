package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
public class ShopUnitStatisticResponseDTO {
    private List<ShopUnitStatisticUnitDTO> items;
}
