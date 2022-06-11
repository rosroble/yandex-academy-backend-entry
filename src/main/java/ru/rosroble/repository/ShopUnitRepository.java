package ru.rosroble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rosroble.model.ShopUnit;

import java.util.*;
import java.util.stream.Collectors;

public interface ShopUnitRepository extends JpaRepository<ShopUnit, String> {
   List<ShopUnit> findShopUnitsByIdIn(Collection<String> id);
   Optional<ShopUnit> findById(String uuid);

   @Modifying
   @Query(value = "DELETE FROM shop_units s WHERE s.id=:uuid", nativeQuery = true)
   void removeById(@Param("uuid") String uuid);

   @Modifying
   @Query(value = "INSERT INTO " +
           "shop_units_history s ('id', 'name', 'parentId', 'type', 'price', 'date')" +
           "values (uuid, name, parentId, type, price, date)", nativeQuery = true)
   void insertToHistoryTable(String uuid, String name, String parentId, String type, Long price, Date date);

   List<ShopUnit> findShopUnitByDateBetween(Date date1, Date date2);

   default Map<String, ShopUnit> findShopUnitsByIdInMap(Collection<String> id) {
      return findShopUnitsByIdIn(id).stream().collect(Collectors.toMap(ShopUnit::getId, x -> x));
   }

}
