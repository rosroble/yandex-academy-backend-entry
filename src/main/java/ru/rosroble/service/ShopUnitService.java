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




    /**
     * Service method for importing entries via POST /imports endpoint.
     * Accepts a validated ShopUnitImportRequestDTO object.
     * The algorithm of importing new / changing old units:
     * 1. Check the ID uniqueness of all incoming units.
     * 2. Try to fetch all incoming units from the database (in case the unit is updated and not created)
     * 3. Try to fetch all the parents of all incoming units from the database
     * 4. Validate and add all the incoming units to a set.
     * 5. Update dates and prices and then save to database.
     * @param dto a dto accepted from the client
     * @throws ValidationException if any of the specified constraints are violated
     */
    public void importNewUnits(ShopUnitImportRequestDTO dto) throws ValidationException {
        List<ShopUnitImportDTO> items = dto.getItems();
        Map<UUID, ShopUnitImportDTO> importSet = items.stream().collect(Collectors.toMap(ShopUnitImportDTO::getId, x -> x, (x1, x2) -> x1));
        if (items.size() != importSet.size()) {
            throw new IdsAreNotUniqueException("Imported items IDs should be unique.");
        }
        Map<UUID, ShopUnit> updatedItems = shopUnitRepository.findShopUnitsByIdInMap(dtoToIdStringList(items));
        updatedItems.putAll(shopUnitRepository.findShopUnitsByIdInMap(dtoToParentStringList(items)));
        Map<UUID, ShopUnit> unitsToAdd = new HashMap<>();
        for (ShopUnitImportDTO item: items) {
            validateAndAdd(dto, item, unitsToAdd, updatedItems, importSet);
        }
        unitsToAdd.putAll(updatedItems);
        Set<ShopUnit> finalPayload = new HashSet<>(unitsToAdd.values());
        updateAncestorsDate(finalPayload, dto.getUpdateDate());
        save(finalPayload);
    }

    /**
     * Calls price update method and then saves changes to database
     * @param finalPayload a set of the units to save/update
     */
    private void save(Set<ShopUnit> finalPayload) {
        Set<ShopUnit> ancestorsOnly = getTopAncestors(finalPayload);
        updateAncestorsPrice(ancestorsOnly);
        shopUnitRepository.saveAll(ancestorsOnly);
        // log ancestors update history ??????????????????
        shopUnitStatisticRepository.saveAll(finalPayload.stream().map(ShopUnit::toShopUnitStatisticUnitDTO).collect(Collectors.toList()));
    }


    /**
     * Method containing the main logic of items validation.<br>
     * The algorithm of validation:<br>
     * 1. Try to find incoming unit among existing ones (the unit could be updated)<br>
     * 2. If it's found, check the client is not trying to change the type, otherwise throw an exception<br>
     * 3. Check the item id and parent id are different, otherwise throw an exception.<br>
     * 4. Parent resolve:<br>
     * 4.1 Try to find item's parent ID among existing units.<br>
     * 4.2 If not found - try to find item's parent among processed incoming units<br>
     * 4.3 If not found - the parent may not be processed yet, try to process it using its id.<br>
     * 4.4 If not found - the parent doesn't exist. Throw an exception.<br>
     * 4.5 If the parent is found and processed ensure its type is "CATEGORY", otherwise throw an exception<br>
     * 5. In case the processed item is a category ensure it <b>doesn't have</b> a price, otherwise throw an exception<br>
     * 6. In case the processed item is an offer ensure it <b>does have</b> a price, otherwise throw an exception<br>
     * 7. If the processed item is a new version of the existing one, then update its attributes<br>
     * 7.1 If the processed item has its parent changed, remove old parent-child relation and add the old parent to a set
     * for further DB update.<br>
     * 8. Else if the processed item is a new unit, then create a new object and add it to a set of new items (unitsToAdd).<br>
     * 9. If the processed item has a parent, assign two-side parent-child relation<br>
     * @param dto a dto containing the list of all processed items and an update date
     * @param item currently processed item
     * @param unitsToAdd a map containing new (not found among existing) processed units
     * @param updatedItems a map containing fetched from the db existing units, that are going to be updated
     * @param importMap ID to ShopUnitImportDTO map, containing all the incoming items
     * @return a validated ShopUnit
     * @throws ValidationException thrown if any of the specified constraints are violated
     */
    private ShopUnit validateAndAdd(ShopUnitImportRequestDTO dto, ShopUnitImportDTO item, Map<UUID, ShopUnit> unitsToAdd, Map<UUID, ShopUnit> updatedItems, Map<UUID, ShopUnitImportDTO> importMap) throws ValidationException{
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
        if (item.getParentId() != null && parent == null && importMap.containsKey(item.getParentId())) {
            // still not found? maybe we process the child first, try to process its parent in the current payload
            parent = validateAndAdd(dto, importMap.get(item.getParentId()), unitsToAdd, updatedItems, importMap);
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
            unit.setDate(dto.getUpdateDate());
            ShopUnit oldParent = unit.getParent();
            // if the parent has changed we need to remove current node from the old parent
            if (oldParent != null && !oldParent.equals(parent)) {
                oldParent.removeChild(unit);
                // add old parent to update payload so the price is re-calculated
                unitsToAdd.put(oldParent.getId(), oldParent);
            }
        } else {
            unit = new ShopUnit(item.getId(), item.getName(), dto.getUpdateDate(), parent, item.getType(), item.getPrice(), null);
            unitsToAdd.put(unit.getId(), unit);
        }
        if (parent != null) {
            parent.addChild(unit);
        }
        return unit;
    }

    public boolean deleteAndUpdateCategoryPrices(UUID uuid) {
        Optional<ShopUnit> optional = shopUnitRepository.findById(uuid);
        if (optional.isEmpty()) return false;
        ShopUnit unit = optional.get();
        delete(unit);
        if (unit.getParent() != null) {
            Set<ShopUnit> set = new HashSet<>();
            set.add(unit);
            Set<ShopUnit> topAncestor = getTopAncestors(set);
            updateAncestorsPrice(topAncestor);
            shopUnitRepository.saveAll(topAncestor);
        }
        return true;
    }

    private void delete(ShopUnit unitToRemove) {
        if (unitToRemove.getChildren() != null) {
            unitToRemove.getChildren().forEach(x -> {
                if (x.getType() == ShopUnitType.OFFER) {
                    shopUnitRepository.removeById(x.getId());
                } else {
                    delete(x);
                }
            });
        }
        shopUnitRepository.removeById(unitToRemove.getId());
    }

    public ShopUnitDTO getNode(UUID uuid) {
        Optional<ShopUnit> optional = shopUnitRepository.findById(uuid);
        return optional.isEmpty() ? null : optional.get().toShopUnitDTO();
    }

    public ShopUnitStatisticResponseDTO sales(Date date) {
        Date dateMinus24 = Date.from(date.toInstant().minus(1, ChronoUnit.DAYS));
        List<ShopUnit> queryResult = shopUnitRepository.findShopUnitByDateBetween(dateMinus24, date);
        return new ShopUnitStatisticResponseDTO(queryResult.stream()
                .filter(x -> x.getType() == ShopUnitType.OFFER)
                .map(ShopUnit::toShopUnitStatisticUnitDTO).toList());
    }

    public ShopUnitStatisticResponseDTO statistic(UUID uuid, Date from, Date to) {
        List<ShopUnitStatisticUnitDTO> queryResult = shopUnitStatisticRepository.getHistoryById(uuid);
        if (queryResult.isEmpty()) return null;
        Stream<ShopUnitStatisticUnitDTO> filteredQuery = queryResult.stream()
                .filter(x -> x.getDate().after(from) && x.getDate().before(to)
                        || x.getDate().equals(from)
                        || x.getDate().equals(to));
        return new ShopUnitStatisticResponseDTO(filteredQuery.toList());
    }

    private List<UUID> dtoToIdStringList(List<ShopUnitImportDTO> list) {
        return list.stream().map(ShopUnitImportDTO::getId).toList();
    }

    private List<UUID> dtoToParentStringList(List<ShopUnitImportDTO> list) {
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
        Set<ShopUnit> topAncestorsSet = getTopAncestors(categorySet);
        topAncestorsSet.forEach(x -> calculateCategoryPrice(x, updatedUnits));
    }

    private Set<ShopUnit> getTopAncestors(Collection<ShopUnit> units) {
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
