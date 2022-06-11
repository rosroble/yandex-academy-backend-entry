package ru.rosroble.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rosroble.dto.*;
import ru.rosroble.exception.*;
import ru.rosroble.model.ShopUnit;
import ru.rosroble.model.ShopUnitType;
import ru.rosroble.repository.ShopUnitRepository;
import ru.rosroble.repository.ShopUnitStatisticRepository;

import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ShopUnitService {

    @Autowired
    ShopUnitRepository shopUnitRepository;

    @Autowired
    ShopUnitStatisticRepository shopUnitStatisticRepository;


    // check id uniqueness
    // check type is not updated
    // check all the parentIds are present
    // check no items have offer as a parent
    public void importNewUnits(ShopUnitImportRequestDTO dto) throws ValidationException {
        List<ShopUnitImportDTO> items = dto.getItems();
        Map<String, ShopUnitImportDTO> importSet = items.stream().collect(Collectors.toMap(ShopUnitImportDTO::getId, x -> x, (x1, x2) -> x1));
        if (items.size() != importSet.size()) {
            throw new IdsAreNotUniqueException("Imported items IDs should be unique.");
        }
        Map<String, ShopUnit> updatedItems = shopUnitRepository.findShopUnitsByIdInMap(dtoToIdStringList(items));
        updatedItems.putAll(shopUnitRepository.findShopUnitsByIdInMap(dtoToParentStringList(items)));
        Map<String, ShopUnit> unitsToAdd = new HashMap<>();
        for (ShopUnitImportDTO item: items) {
            validateAndAdd(dto, item, unitsToAdd, updatedItems, importSet);
        }
        unitsToAdd.putAll(updatedItems);
        Set<ShopUnit> finalPayload = new HashSet<>(unitsToAdd.values());
        save(finalPayload, dto.getUpdateDate());
    }

    public void save(Set<ShopUnit> finalPayload, Date updateDate) {
        shopUnitRepository.saveAll(finalPayload);
        updateAncestorsDate(finalPayload, updateDate);
        updateAncestorsPrice(finalPayload);
        shopUnitRepository.saveAll(finalPayload);
        shopUnitStatisticRepository.saveAll(finalPayload.stream().map(ShopUnit::toShopUnitStatisticUnitDTO).collect(Collectors.toList()));
       // shopUnitRepository.insertToHistoryTable(finalPayload);
    }

    public ShopUnit validateAndAdd(ShopUnitImportRequestDTO dto, ShopUnitImportDTO item, Map<String, ShopUnit> unitsToAdd, Map<String, ShopUnit> updatedItems, Map<String, ShopUnitImportDTO> importSet) throws ValidationException{
        if (unitsToAdd.containsKey(item.getId())) return null;
        ShopUnit dbItem = updatedItems.get(item.getId());
        if ((dbItem != null) && (item.getType() != dbItem.getType())){
            throw new TypeChangeAttemptException("Changing unit type is not allowed");
        }

        if (item.getId().equals(item.getParentId())) {
            throw new BadParentException("An item cannot be its parent");
        }

        ShopUnit parent = updatedItems.get(item.getParentId()); // search among existing entries
        if (parent == null) {
            parent = unitsToAdd.get(item.getParentId()); // search among current payload
        }
        if (item.getParentId() != null && parent == null && importSet.containsKey(item.getParentId())) {
            // still not found? maybe we process the child first, try to process its parent in the current payload
            parent = validateAndAdd(dto, importSet.get(item.getParentId()), unitsToAdd, updatedItems, importSet);
        }
        if ((parent == null) && (item.getParentId() != null)) {
            throw new BadParentException("Parent does not exist.");
        }
        if ((parent != null) && (parent.getType() == ShopUnitType.OFFER)) {
            throw new BadParentException("Offer type cannot be a parent");
        }
        if (item.getType() == ShopUnitType.CATEGORY && item.getPrice() != null) {
            throw new BadPriceException("Category should not have a price");
        }
        if (item.getType() == ShopUnitType.OFFER && item.getPrice() == null) {
            throw new BadPriceException("Offer must have a price");
        }

        ShopUnit unit;
        if (updatedItems.containsKey(item.getId())) {
            unit = updatedItems.get(item.getId());
            unit.setPrice(item.getPrice());
            unit.setName(item.getName());
          //  unit.setDate(dto.getUpdateDate());
        } else {
            unit = new ShopUnit(item.getId(), item.getName(), dto.getUpdateDate(), parent, item.getType(), item.getPrice(), null);
            unitsToAdd.put(unit.getId(), unit);
        }
        if (parent != null) {
            parent.addChild(unit);
        }
        return unit;
    }

    public boolean delete(String uuid) {
        Optional<ShopUnit> optional = shopUnitRepository.findById(uuid);
        if (optional.isEmpty()) return false;
        ShopUnit unit = optional.get();
        if (unit.getChildren() != null) {
            unit.getChildren().forEach(x -> {
                if (x.getType() == ShopUnitType.OFFER) {
                    shopUnitRepository.removeById(x.getId());
                } else {
                    delete(x.getId());
                }
            });
        }
        shopUnitRepository.removeById(uuid);
        return true;
    }

    public ShopUnitDTO getNode(String uuid) {
        Optional<ShopUnit> optional = shopUnitRepository.findById(uuid);
        return optional.isEmpty() ? null : optional.get().toShopUnitDTO();
    }

    public ShopUnitStatisticResponseDTO sales(Date date) {
        Date dateMinus24 = Date.from(date.toInstant().minus(1, ChronoUnit.DAYS));
        List<ShopUnit> queryResult = shopUnitRepository.findShopUnitByDateBetween(dateMinus24, date);
        return new ShopUnitStatisticResponseDTO(queryResult.stream().map(ShopUnit::toShopUnitStatisticUnitDTO).toList());
    }

    public ShopUnitStatisticResponseDTO statistic(String uuid, Date from, Date to) {
        List<ShopUnitStatisticUnitDTO> queryResult = shopUnitStatisticRepository.getHistoryById(uuid);
        if (queryResult.isEmpty()) return null;
        Stream<ShopUnitStatisticUnitDTO> filteredQuery = queryResult.stream()
                .filter(x -> x.getDate().after(from) && x.getDate().before(to)
                        || x.getDate().equals(from)
                        || x.getDate().equals(to));
        return new ShopUnitStatisticResponseDTO(filteredQuery.toList());
    }

    private List<String> dtoToIdStringList(List<ShopUnitImportDTO> list) {
        return list.stream().map(ShopUnitImportDTO::getId).toList();
    }

    private List<String> dtoToParentStringList(List<ShopUnitImportDTO> list) {
        return list.stream().map(ShopUnitImportDTO::getParentId).toList();
    }

    private void updateAncestorsDate(Collection<ShopUnit> units, Date newDate) {
        Set<ShopUnit> updatedUnits = new HashSet<>();
        for (ShopUnit unit: units) {
            updateDate(unit, newDate, updatedUnits);
        }
    }

    private void updateAncestorsPrice(Collection<ShopUnit> units) {
        Set<ShopUnit> updatedUnits = new HashSet<>();
        Set<ShopUnit> categorySet = units.stream().filter(x -> x.getType() == ShopUnitType.CATEGORY).collect(Collectors.toSet());
        Set<ShopUnit> topAncestorsSet = appendAncestors(categorySet);
       // units.addAll(topAncestorsSet);
        topAncestorsSet.forEach(x -> calculateCategoryPrice(x, updatedUnits));
    }

    private Set<ShopUnit> appendAncestors(Collection<ShopUnit> units) {
        Set<ShopUnit> topAncestorSet = new HashSet<>();
        Set<ShopUnit> visited = new HashSet<>();
        OUTER_LOOP: for (ShopUnit unit: units) {
            ShopUnit current = unit;
            while (current.getParent() != null) {
                if (visited.contains(current)) continue OUTER_LOOP;
                visited.add(current);
                current = current.getParent();
            }
            topAncestorSet.add(current);
        }
        return topAncestorSet;
    }

    private void updateDate(ShopUnit unit, Date newDate, Set<ShopUnit> updatedUnits) {
        if (updatedUnits.contains(unit)) return;
        unit.setDate(newDate);
        updatedUnits.add(unit);
        if (unit.getParent() != null) {
            updateDate(unit.getParent(), newDate, updatedUnits);
        }
    }

    private PriceCalculationResult calculateCategoryPrice(ShopUnit unit, Set<ShopUnit> updated) {
        if ((unit.getType() == ShopUnitType.OFFER)
                || (unit.getChildren() == null)
                || (unit.getChildren().isEmpty())
                || updated.contains(unit)) {
            return null;
        }
        updated.add(unit);
        PriceCalculationResult result = new PriceCalculationResult(0L, 0L);
        HashSet<ShopUnit> children = new HashSet<>(unit.getChildren());
        for (ShopUnit child: children) {
            if (child.getType() == ShopUnitType.CATEGORY) {
                result.add(calculateCategoryPrice(child, updated));
            } else {
                result.incrementCount(1L);
                result.incrementSum(child.getPrice());
            }
        }
        unit.setPrice(result.getSum() / result.getCount());
        return result;
    }
}
