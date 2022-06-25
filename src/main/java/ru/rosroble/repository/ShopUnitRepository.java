package ru.rosroble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rosroble.model.ShopUnit;

import java.util.*;
import java.util.stream.Collectors;

public interface ShopUnitRepository extends JpaRepository<ShopUnit, String> {
   List<ShopUnit> findShopUnitsByIdIn(Collection<UUID> id);
   Optional<ShopUnit> findById(UUID uuid);

   @Modifying
   @Query(value = "DELETE FROM shop_units s WHERE s.id=:uuid", nativeQuery = true)
   void removeById(@Param("uuid") UUID uuid);

   @Modifying
   @Query(value = "INSERT INTO " +
           "shop_units_history (id, name, p_id, type, price, date) " +
           "values (:id, :name, :p_id, :type, :price, :date)", nativeQuery = true)
   void insertToHistoryTable(@Param("id") UUID id,
                             @Param("name") String name,
                             @Param("p_id") UUID parentId,
                             @Param("type") String type,
                             @Param("price") Long price,
                             @Param("date") Date date);

   default void insertToHistoryTable(Set<ShopUnit> units) {
      units.forEach(x -> insertToHistoryTable(
              x.getId(),
              x.getName(),
              x.getParent() == null ? null : x.getParent().getId(),
              x.getType().name(),
              x.getPrice(),
              x.getDate()));
   }

   List<ShopUnit> findShopUnitByDateBetween(Date date1, Date date2);

   default Map<UUID, ShopUnit> findShopUnitsByIdInMap(Collection<UUID> id) {
      return findShopUnitsByIdIn(id).stream().collect(Collectors.toMap(ShopUnit::getId, x -> x));
   }

   void deleteAll();

}
