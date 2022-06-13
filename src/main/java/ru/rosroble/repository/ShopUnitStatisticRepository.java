package ru.rosroble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rosroble.dto.ShopUnitStatisticUnitDTO;

import java.util.Date;
import java.util.List;

public interface ShopUnitStatisticRepository extends JpaRepository<ShopUnitStatisticUnitDTO, Integer> {
    @Query(value = "SELECT DISTINCT * FROM shop_units_history s WHERE s.id=:uuid", nativeQuery = true)
    List<ShopUnitStatisticUnitDTO> getHistoryById(String uuid);
    List<ShopUnitStatisticUnitDTO> getAllById(String uuid);
}