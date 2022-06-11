package ru.rosroble.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ShopUnitImportRequestDTO {
    @NotNull(message = "List of units expected.")
    private List<@Valid ShopUnitImportDTO> items;
    @NotNull(message = "Date should not be null.")
    private Date updateDate;
}
