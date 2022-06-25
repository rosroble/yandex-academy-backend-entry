package ru.rosroble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rosroble.dto.ShopUnitStatisticUnitDTO;

import java.util.List;
import java.util.UUID;

public interface ShopUnitStatisticRepository extends JpaRepository<ShopUnitStatisticUnitDTO, Integer> {
    @Query(value = "SELECT DISTINCT * FROM shop_units_history s WHERE s.id=:uuid", nativeQuery = true)
    List<ShopUnitStatisticUnitDTO> getHistoryById(UUID uuid);
    List<ShopUnitStatisticUnitDTO> getAllById(UUID uuid);

    void removeById(UUID id);

}
